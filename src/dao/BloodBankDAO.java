package dao;

import model.BloodBank;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BloodBankDAO {

    public List<BloodBank> getAll() throws SQLException {
        List<BloodBank> list = new ArrayList<>();
        String sql = "SELECT * FROM BLOOD_BANK ORDER BY blood_group, distance_km ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                BloodBank b = new BloodBank();
                b.setBankId(rs.getInt("bank_id"));
                b.setName(rs.getString("name"));
                b.setLocation(rs.getString("location"));
                b.setDistanceKm(rs.getDouble("distance_km"));
                b.setBloodGroup(rs.getString("blood_group"));
                b.setUnitsAvailable(rs.getInt("units_available"));
                list.add(b);
            }
        }
        return list;
    }

    public List<BloodBank> getByBloodGroup(String bloodGroup) throws SQLException {
        List<BloodBank> list = new ArrayList<>();
        String sql = "SELECT * FROM BLOOD_BANK WHERE blood_group = ? ORDER BY distance_km ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bloodGroup);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BloodBank b = new BloodBank();
                    b.setBankId(rs.getInt("bank_id"));
                    b.setName(rs.getString("name"));
                    b.setLocation(rs.getString("location"));
                    b.setDistanceKm(rs.getDouble("distance_km"));
                    b.setBloodGroup(rs.getString("blood_group"));
                    b.setUnitsAvailable(rs.getInt("units_available"));
                    list.add(b);
                }
            }
        }
        return list;
    }

    public BloodBank findNearestWithStock(String bloodGroup, int minUnits) throws SQLException {
        String sql = "SELECT * FROM BLOOD_BANK WHERE blood_group = ? AND units_available >= ? ORDER BY distance_km ASC LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bloodGroup);
            ps.setInt(2, minUnits);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BloodBank b = new BloodBank();
                    b.setBankId(rs.getInt("bank_id"));
                    b.setName(rs.getString("name"));
                    b.setLocation(rs.getString("location"));
                    b.setDistanceKm(rs.getDouble("distance_km"));
                    b.setBloodGroup(rs.getString("blood_group"));
                    b.setUnitsAvailable(rs.getInt("units_available"));
                    return b;
                }
            }
        }
        return null; 
    }

    public void reduceUnits(int bankId, int unitsUsed) throws SQLException {
        String sql = "UPDATE BLOOD_BANK SET units_available = units_available - ? WHERE bank_id = ? AND units_available >= ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, unitsUsed);
            ps.setInt(2, bankId);
            ps.setInt(3, unitsUsed); 
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Insufficient blood units or bank not found for ID: " + bankId);
            }
        }
    }
}