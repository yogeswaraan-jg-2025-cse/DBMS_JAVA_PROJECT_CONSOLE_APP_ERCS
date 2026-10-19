package model;

public class Emergency implements Comparable<Emergency> {
    private int emergencyId;
    private int patientId;
    private String emergencyType;
    private String severity;
    private String location;
    private int priorityScore;
    private String status;

    public Emergency() {}
    public int getEmergencyId() { return emergencyId; }
    public void setEmergencyId(int emergencyId) { this.emergencyId = emergencyId; }
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public String getEmergencyType() { return emergencyType; }
    public void setEmergencyType(String emergencyType) { this.emergencyType = emergencyType; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public int getPriorityScore() { return priorityScore; }
    public void setPriorityScore(int priorityScore) { this.priorityScore = priorityScore; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public int compareTo(Emergency other) {
        if (this.priorityScore != other.priorityScore) {
            return Integer.compare(other.priorityScore, this.priorityScore);
        }
        if (getSeverityRank(this.severity) != getSeverityRank(other.severity)) {
            return Integer.compare(getSeverityRank(other.severity), getSeverityRank(this.severity));
        }
        return Integer.compare(this.emergencyId, other.emergencyId);
    }

    private int getSeverityRank(String sev) {
        switch (sev) {
            case "CRITICAL": return 4;
            case "HIGH": return 3;
            case "MEDIUM": return 2;
            case "LOW": return 1;
            default: return 0;
        }
    }
}
