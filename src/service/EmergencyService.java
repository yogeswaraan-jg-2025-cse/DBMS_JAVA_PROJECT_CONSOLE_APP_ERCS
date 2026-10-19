package service;
import model.*;
import dao.*;
import java.sql.SQLException;
import java.sql.Connection;
import util.DBConnection;
import java.util.*;

public class EmergencyService {
    private PatientDAO patientDAO = new PatientDAO();
    private EmergencyDAO emergencyDAO = new EmergencyDAO();
    private PriorityRuleDAO priorityRuleDAO = new PriorityRuleDAO();
    private PriorityQueue<Emergency> priorityQueue = new PriorityQueue<>();

    public Emergency reportEmergency(Patient patient, String type, String location, String condition) throws SQLException {
        int score = calculatePriorityScore(patient, condition);
        String severity = classifySeverity(score);

        Emergency em = new Emergency();
        em.setEmergencyType(type);
        em.setLocation(location);
        em.setPriorityScore(score);
        em.setSeverity(severity);
        em.setStatus("PENDING");

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int pid = patientDAO.insert(patient, conn);
                patient.setPatientId(pid);
                em.setPatientId(pid);
                int eid = emergencyDAO.insert(em, conn);
                em.setEmergencyId(eid);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
        priorityQueue.offer(em);
        return em;
    }

    private int calculatePriorityScore(Patient p, String condition) throws SQLException {
        int score = 0;
        String cond = condition.toLowerCase();
        for (PriorityRule rule : priorityRuleDAO.getActiveRules()) {
            if ("BLOOD_LOSS".equals(rule.getRuleType()) && rule.getKeyword().equals(p.getBloodLoss())) {
                score += rule.getPoints();
            } else if ("CONDITION".equals(rule.getRuleType()) && cond.contains(rule.getKeyword().toLowerCase())) {
                score += rule.getPoints();
            }
        }
        return score;
    }

    private String classifySeverity(int score) {
        if (score >= 10) return "CRITICAL";
        if (score >= 6) return "HIGH";
        if (score >= 3) return "MEDIUM";
        return "LOW";
    }

    public PriorityQueue<Emergency> getPriorityQueue() { return priorityQueue; }

    public Patient getPatient(int patientId) throws SQLException { return patientDAO.findById(patientId); }
    
    public void loadPendingToQueue() throws SQLException {
        priorityQueue.clear();
        priorityQueue.addAll(emergencyDAO.getAllPending());
    }
}
