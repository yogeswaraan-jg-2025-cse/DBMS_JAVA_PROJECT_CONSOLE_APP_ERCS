package dao;

import model.PriorityRule;
import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PriorityRuleDAO {
    public List<PriorityRule> getActiveRules() throws SQLException {
        String sql = "SELECT rule_type, keyword, points FROM PRIORITY_RULE WHERE active = TRUE";
        List<PriorityRule> rules = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PriorityRule rule = new PriorityRule();
                rule.setRuleType(rs.getString("rule_type"));
                rule.setKeyword(rs.getString("keyword"));
                rule.setPoints(rs.getInt("points"));
                rules.add(rule);
            }
        }
        return rules;
    }
}
