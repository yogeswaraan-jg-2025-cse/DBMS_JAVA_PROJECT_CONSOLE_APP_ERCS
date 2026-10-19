package service;
import model.*;
import dao.*;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import util.DBConnection;
import java.sql.Connection;

public class ResourceService {
    private AmbulanceDAO ambulanceDAO = new AmbulanceDAO();
    private HospitalDAO hospitalDAO = new HospitalDAO();
    private BloodBankDAO bloodBankDAO = new BloodBankDAO();
    private ResponseDAO responseDAO = new ResponseDAO();

    public Ambulance findBestAmbulance() throws SQLException {
        List<Ambulance> all = ambulanceDAO.getAllAvailable();
        return all.stream()
                  .min(Comparator.comparingDouble(Ambulance::getDistanceKm))
                  .orElse(null);
    }

    public Hospital findBestHospital(boolean needsTrauma, boolean needsICU) throws SQLException {
        List<Hospital> all = hospitalDAO.getAll();
        return all.stream()
            .filter(h -> {
                if (needsTrauma && !h.isTraumaAvailable()) return false;
                if (needsICU && h.getIcuBeds() <= 0) return false;
                return h.isEmergencyAvailable();
            })
            .max(Comparator.comparingDouble(h -> {
                double score = 100 - h.getDistanceKm();
                if (h.isTraumaAvailable()) score += 20;
                if (h.getIcuBeds() > 0) score += 15;
                return score;
            }))
            .orElse(null);
    }

    public BloodBank findMatchingBloodBank(String bloodGroup, boolean urgent) throws SQLException {
        List<BloodBank> matches = bloodBankDAO.getByBloodGroup(bloodGroup);
        matches = matches.stream().filter(b -> b.getUnitsAvailable() > 0).collect(Collectors.toList());
        return matches.stream()
                      .min(Comparator.comparingDouble(BloodBank::getDistanceKm))
                      .orElse(null);
    }

    public String saveResponsePlan(Emergency emergency, Ambulance ambulance, Hospital hospital, BloodBank bloodBank) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                boolean dispatchReady = ambulance != null && hospital != null;
                String responseStatus = dispatchReady ? "PLANNED" : "PARTIAL";
                responseDAO.insert(conn, emergency.getEmergencyId(),
                    ambulance == null ? null : ambulance.getAmbulanceId(),
                    hospital == null ? null : hospital.getHospitalId(),
                    bloodBank == null ? null : bloodBank.getBankId(), responseStatus);
                if (dispatchReady) {
                    try (java.sql.PreparedStatement ps = conn.prepareStatement("UPDATE AMBULANCE SET status = 'BUSY' WHERE ambulance_id = ?")) {
                        ps.setInt(1, ambulance.getAmbulanceId());
                        ps.executeUpdate();
                    }
                }
                try (java.sql.PreparedStatement ps = conn.prepareStatement("UPDATE EMERGENCY SET status = 'PLANNED' WHERE emergency_id = ?")) {
                    if (dispatchReady) {
                        ps.setInt(1, emergency.getEmergencyId());
                        ps.executeUpdate();
                    }
                }
                conn.commit();
                return responseStatus;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public boolean completeResponse(int emergencyId) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Integer ambulanceId = responseDAO.findPlannedAmbulanceId(conn, emergencyId);
                if (ambulanceId == null) {
                    conn.rollback();
                    return false;
                }
                try (java.sql.PreparedStatement ps = conn.prepareStatement("UPDATE AMBULANCE SET status = 'AVAILABLE' WHERE ambulance_id = ?")) {
                    ps.setInt(1, ambulanceId);
                    ps.executeUpdate();
                }
                try (java.sql.PreparedStatement ps = conn.prepareStatement("UPDATE RESPONSE SET response_status = 'COMPLETED' WHERE emergency_id = ? AND response_status = 'PLANNED'")) {
                    ps.setInt(1, emergencyId);
                    ps.executeUpdate();
                }
                try (java.sql.PreparedStatement ps = conn.prepareStatement("UPDATE EMERGENCY SET status = 'COMPLETED' WHERE emergency_id = ?")) {
                    ps.setInt(1, emergencyId);
                    ps.executeUpdate();
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
}
