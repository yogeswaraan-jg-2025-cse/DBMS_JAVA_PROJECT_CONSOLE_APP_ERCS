package model;
public class ResponsePlan {
    private Emergency emergency;
    private Ambulance ambulance;
    private Hospital hospital;
    private BloodBank bloodBank;
    private String bloodStatus;
    private int requiredUnits;
    private String reason;

    public ResponsePlan(Emergency e, Ambulance a, Hospital h, BloodBank b, String bs, int ru, String r){
        this.emergency=e; this.ambulance=a; this.hospital=h; this.bloodBank=b;
        this.bloodStatus=bs; this.requiredUnits=ru; this.reason=r;
    }
    public Emergency getEmergency(){return emergency;}
    public Ambulance getAmbulance(){return ambulance;}
    public Hospital getHospital(){return hospital;}
    public BloodBank getBloodBank(){return bloodBank;}
    public String getBloodStatus(){return bloodStatus;}
    public int getRequiredUnits(){return requiredUnits;}
    public String getReason(){return reason;}
}