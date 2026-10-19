package dao;
import model.Patient;
import util.DBConnection;
import java.sql.*;

public class PatientDAO {
    public int insert(Patient p) throws SQLException {
        String sql = "INSERT INTO PATIENT (name, age, blood_group, blood_loss, condition_description) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            return insert(p, ps);
        }
    }

    public int insert(Patient p, Connection conn) throws SQLException {
        String sql = "INSERT INTO PATIENT (name, age, blood_group, blood_loss, condition_description) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            return insert(p, ps);
        }
    }

    private int insert(Patient p, PreparedStatement ps) throws SQLException {
            ps.setString(1, p.getName());
            ps.setInt(2, p.getAge());
            ps.setString(3, p.getBloodGroup());
            ps.setString(4, p.getBloodLoss());
            ps.setString(5, p.getConditionDescription());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        throw new SQLException("Failed to insert patient");
    }

    public Patient findById(int patientId) throws SQLException {
        String sql = "SELECT * FROM PATIENT WHERE patient_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Patient p = new Patient();
                p.setPatientId(rs.getInt("patient_id"));
                p.setName(rs.getString("name"));
                p.setAge(rs.getInt("age"));
                p.setBloodGroup(rs.getString("blood_group"));
                p.setBloodLoss(rs.getString("blood_loss"));
                p.setConditionDescription(rs.getString("condition_description"));
                return p;
            }
        }
    }
}
