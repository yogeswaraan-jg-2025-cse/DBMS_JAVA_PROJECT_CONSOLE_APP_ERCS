package dao;

import model.Ambulance;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AmbulanceDAO {

    public List<Ambulance> getAll() throws SQLException {
        List<Ambulance> list = new ArrayList<>();
        String sql = "SELECT * FROM AMBULANCE ORDER BY distance_km ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ambulance a = new Ambulance();
                a.setAmbulanceId(rs.getInt("ambulance_id"));
                a.setAmbulanceNumber(rs.getString("ambulance_number"));
                a.setLocation(rs.getString("location"));
                a.setDistanceKm(rs.getDouble("distance_km"));
                a.setAmbulanceType(rs.getString("ambulance_type"));
                a.setStatus(rs.getString("status"));
                list.add(a);
            }
        }
        return list;
    }

    public List<Ambulance> getAllAvailable() throws SQLException {
        List<Ambulance> list = new ArrayList<>();
        String sql = "SELECT * FROM AMBULANCE WHERE status = 'AVAILABLE' ORDER BY distance_km ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ambulance a = new Ambulance();
                a.setAmbulanceId(rs.getInt("ambulance_id"));
                a.setAmbulanceNumber(rs.getString("ambulance_number"));
                a.setLocation(rs.getString("location"));
                a.setDistanceKm(rs.getDouble("distance_km"));
                a.setAmbulanceType(rs.getString("ambulance_type"));
                a.setStatus(rs.getString("status"));
                list.add(a);
            }
        }
        return list;
    }

    public Ambulance findNearestAvailable(String preferredType) throws SQLException {
        // Try preferred type first, fall back to any available
        String sql = "SELECT * FROM AMBULANCE WHERE status = 'AVAILABLE' " +
                     "ORDER BY CASE WHEN ambulance_type = ? THEN 0 ELSE 1 END, distance_km ASC LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, preferredType);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Ambulance a = new Ambulance();
                    a.setAmbulanceId(rs.getInt("ambulance_id"));
                    a.setAmbulanceNumber(rs.getString("ambulance_number"));
                    a.setLocation(rs.getString("location"));
                    a.setDistanceKm(rs.getDouble("distance_km"));
                    a.setAmbulanceType(rs.getString("ambulance_type"));
                    a.setStatus(rs.getString("status"));
                    return a;
                }
            }
        }
        return null; // No ambulance available
    }

    public void updateStatus(int ambulanceId, String newStatus) throws SQLException {
        String sql = "UPDATE AMBULANCE SET status = ? WHERE ambulance_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, ambulanceId);
            ps.executeUpdate();
        }
    }
}