package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ResponseDAO {
    public int insert(Connection conn, int emergencyId, Integer ambulanceId, Integer hospitalId,
                      Integer bloodBankId, String status) throws SQLException {
        String sql = "INSERT INTO RESPONSE (emergency_id, ambulance_id, hospital_id, blood_bank_id, response_status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, emergencyId);
            setNullableInt(ps, 2, ambulanceId);
            setNullableInt(ps, 3, hospitalId);
            setNullableInt(ps, 4, bloodBankId);
            ps.setString(5, status);
            ps.executeUpdate();
            try (java.sql.ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("Failed to save response record");
    }

    private void setNullableInt(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null) ps.setNull(index, java.sql.Types.INTEGER);
        else ps.setInt(index, value);
    }

    public Integer findPlannedAmbulanceId(Connection conn, int emergencyId) throws SQLException {
        String sql = "SELECT ambulance_id FROM RESPONSE WHERE emergency_id = ? AND response_status = 'PLANNED' "
                   + "AND ambulance_id IS NOT NULL ORDER BY response_id DESC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, emergencyId);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("ambulance_id") : null;
            }
        }
    }
}
