package model;
public class Hospital {
    private int hospitalId; 
	private String name;
	private String location;
    private double distanceKm;
	private int icuBeds; 
	private boolean traumaAvailable;
	private boolean emergencyAvailable;
    public Hospital(){} 
    public int getHospitalId(){return hospitalId;}
	public void setHospitalId(int v){this.hospitalId=v;}
    public String getName(){return name;} 
	public void setName(String v){this.name=v;}
    public String getLocation(){return location;} 
	public void setLocation(String v){this.location=v;}
    public double getDistanceKm(){return distanceKm;} 
	public void setDistanceKm(double v){this.distanceKm=v;}
    public int getIcuBeds(){return icuBeds;} 
	public void setIcuBeds(int v){this.icuBeds=v;}
    public boolean isTraumaAvailable(){return traumaAvailable;} 
	public void setTraumaAvailable(boolean v){this.traumaAvailable=v;}
    public boolean isEmergencyAvailable(){return emergencyAvailable;}
	public void setEmergencyAvailable(boolean v){this.emergencyAvailable=v;}
}