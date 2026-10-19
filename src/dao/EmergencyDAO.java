package dao;
import model.Emergency;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmergencyDAO {
    public int insert(Emergency e) throws SQLException {
        String sql = "INSERT INTO EMERGENCY (patient_id, emergency_type, severity, location, priority_score, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            return insert(e, ps);
        }
    }

    public int insert(Emergency e, Connection conn) throws SQLException {
        String sql = "INSERT INTO EMERGENCY (patient_id, emergency_type, severity, location, priority_score, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            return insert(e, ps);
        }
    }

    private int insert(Emergency e, PreparedStatement ps) throws SQLException {
            ps.setInt(1, e.getPatientId());
            ps.setString(2, e.getEmergencyType());
            ps.setString(3, e.getSeverity());
            ps.setString(4, e.getLocation());
            ps.setInt(5, e.getPriorityScore());
            ps.setString(6, e.getStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        throw new SQLException("Failed to insert emergency");
    }

    public void updateStatus(int emergencyId, String status) throws SQLException {
        String sql = "UPDATE EMERGENCY SET status = ? WHERE emergency_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, emergencyId);
            ps.executeUpdate();
        }
    }

    public List<Emergency> getAllPending() throws SQLException {
        List<Emergency> list = new ArrayList<>();
        String sql = "SELECT * FROM EMERGENCY WHERE status = 'PENDING' ORDER BY priority_score DESC, emergency_id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Emergency e = new Emergency();
                e.setEmergencyId(rs.getInt("emergency_id"));
                e.setPatientId(rs.getInt("patient_id"));
                e.setEmergencyType(rs.getString("emergency_type"));
                e.setSeverity(rs.getString("severity"));
                e.setLocation(rs.getString("location"));
                e.setPriorityScore(rs.getInt("priority_score"));
                e.setStatus(rs.getString("status"));
                list.add(e);
            }
        }
        return list;
    }
}
