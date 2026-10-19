package model;
public class Ambulance {
    private int ambulanceId; private String ambulanceNumber; private String location;
    private double distanceKm; private String ambulanceType; private String status;
    public Ambulance(){}
    public int getAmbulanceId(){return ambulanceId;} public void setAmbulanceId(int v){this.ambulanceId=v;}
    public String getAmbulanceNumber(){return ambulanceNumber;} public void setAmbulanceNumber(String v){this.ambulanceNumber=v;}
    public String getLocation(){return location;} public void setLocation(String v){this.location=v;}
    public double getDistanceKm(){return distanceKm;} public void setDistanceKm(double v){this.distanceKm=v;}
    public String getAmbulanceType(){return ambulanceType;} public void setAmbulanceType(String v){this.ambulanceType=v;}
    public String getStatus(){return status;} public void setStatus(String v){this.status=v;}
}