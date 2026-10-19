package model;
public class BloodBank {
    private int bankId; private String name; private String location;
    private double distanceKm; private String bloodGroup; private int unitsAvailable;
    public BloodBank(){}
    public int getBankId(){return bankId;} public void setBankId(int v){this.bankId=v;}
    public String getName(){return name;} public void setName(String v){this.name=v;}
    public String getLocation(){return location;} public void setLocation(String v){this.location=v;}
    public double getDistanceKm(){return distanceKm;} public void setDistanceKm(double v){this.distanceKm=v;}
    public String getBloodGroup(){return bloodGroup;} public void setBloodGroup(String v){this.bloodGroup=v;}
    public int getUnitsAvailable(){return unitsAvailable;} public void setUnitsAvailable(int v){this.unitsAvailable=v;}
}