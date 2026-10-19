package dao;

import model.Hospital;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HospitalDAO {

    public List<Hospital> getAll() throws SQLException {
        List<Hospital> list = new ArrayList<>();
        String sql = "SELECT * FROM HOSPITAL ORDER BY distance_km ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Hospital h = new Hospital();
                h.setHospitalId(rs.getInt("hospital_id"));
                h.setName(rs.getString("name"));
                h.setLocation(rs.getString("location"));
                h.setDistanceKm(rs.getDouble("distance_km"));
                h.setIcuBeds(rs.getInt("icu_beds"));
                h.setTraumaAvailable(rs.getBoolean("trauma_available"));
                h.setEmergencyAvailable(rs.getBoolean("emergency_available"));
                list.add(h);
            }
        }
        return list;
    }

    public Hospital findNearestAvailable(boolean needsTrauma, boolean needsICU) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT * FROM HOSPITAL WHERE emergency_available = TRUE"
        );
        if (needsTrauma) sql.append(" AND trauma_available = TRUE");
        if (needsICU) sql.append(" AND icu_beds > 0");
        sql.append(" ORDER BY distance_km ASC LIMIT 1");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString());
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                Hospital h = new Hospital();
                h.setHospitalId(rs.getInt("hospital_id"));
                h.setName(rs.getString("name"));
                h.setLocation(rs.getString("location"));
                h.setDistanceKm(rs.getDouble("distance_km"));
                h.setIcuBeds(rs.getInt("icu_beds"));
                h.setTraumaAvailable(rs.getBoolean("trauma_available"));
                h.setEmergencyAvailable(rs.getBoolean("emergency_available"));
                return h;
            }
        }
        return null; // No suitable hospital found
    }

    public void updateIcuBeds(int hospitalId, int newBedCount) throws SQLException {
        String sql = "UPDATE HOSPITAL SET icu_beds = ? WHERE hospital_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newBedCount);
            ps.setInt(2, hospitalId);
            ps.executeUpdate();
        }
    }
}