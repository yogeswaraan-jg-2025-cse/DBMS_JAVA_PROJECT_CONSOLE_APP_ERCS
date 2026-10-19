package model;

public class Patient {
    private int patientId;
    private String name;
    private int age;
    private String bloodGroup;
    private String bloodLoss;
    private String conditionDescription;

    public Patient() {}

    public Patient(String name, int age, String bloodGroup, String bloodLoss, String conditionDescription) {
        this.name = name;
        this.age = age;
        this.bloodGroup = bloodGroup;
        this.bloodLoss = bloodLoss;
        this.conditionDescription = conditionDescription;
    }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public String getBloodLoss() { return bloodLoss; }
    public void setBloodLoss(String bloodLoss) { this.bloodLoss = bloodLoss; }
    public String getConditionDescription() { return conditionDescription; }
    public void setConditionDescription(String conditionDescription) { this.conditionDescription = conditionDescription; }
}