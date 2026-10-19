import model.*;
import service.*;
import util.*;
import dao.*;
import java.sql.SQLException;
import java.util.PriorityQueue;
import java.util.List;

public class Main {
    private static EmergencyService emergencyService = new EmergencyService();
    private static ResourceService resourceService = new ResourceService();

    public static void main(String[] args) {
        try {
            
            emergencyService.loadPendingToQueue();
            
            boolean running = true;
            while (running) {
                printMenu();
                int choice = InputValidator.getIntInRange("Choose: ", 0, 10);
                switch (choice) {
                    case 1: reportEmergency(); break;
                    case 2: analyzeNextEmergency(); break;
                    case 3: viewPriorityQueue(); break;
                    case 4: findAmbulance(); break;
                    case 5: findHospital(); break;
                    case 6: checkBlood(); break;
                    case 7: generateResponsePlan(); break;
                    case 8: viewActiveEmergencies(); break;
                    case 9: viewResources(); break;
                    case 10: completeResponse(); break;
                    case 0: 
                        running = false; 
                        System.out.println("Exiting ERCS...");
                        break;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Database Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            util.DBConnection.closeConnection();
        }
    }

    private static void printMenu() {
        System.out.println("\n=== ERCS MENU ===");
        System.out.println("1. Report Emergency");
        System.out.println("2. Analyze Next Emergency");
        System.out.println("3. View Priority Queue");
        System.out.println("4. Find Ambulance");
        System.out.println("5. Find Hospital");
        System.out.println("6. Check Blood Availability");
        System.out.println("7. Generate Full Response Plan");
        System.out.println("8. View Active Emergencies");
        System.out.println("9. View Resources");
        System.out.println("10. Complete Response / Release Ambulance");
        System.out.println("0. Exit");
    }

    private static void reportEmergency() throws SQLException {
        System.out.println("--- REPORT NEW EMERGENCY ---");
        String name = InputValidator.getNonEmptyString("Patient Name: ");
        int age = InputValidator.getIntInRange("Age: ", 0, 120);
        String bg = InputValidator.getValidBloodGroup();
        String bl = InputValidator.getValidBloodLoss();
        String cond = InputValidator.getNonEmptyString("Condition Description: ");
        String type = InputValidator.getNonEmptyString("Emergency Type (Accident/Cardiac/etc): ");
        String loc = InputValidator.getNonEmptyString("Location: ");

        Patient p = new Patient(name, age, bg, bl, cond);
        Emergency e = emergencyService.reportEmergency(p, type, loc, cond);
        System.out.println("✅ Emergency Reported Successfully!");
        System.out.println("   ID: ER" + e.getEmergencyId() + " | Severity: " + e.getSeverity() + " | Score: " + e.getPriorityScore());
    }

    private static void analyzeNextEmergency() {
        PriorityQueue<Emergency> pq = emergencyService.getPriorityQueue();
        if (pq.isEmpty()) {
            System.out.println("⚠️ No pending emergencies in the queue.");
        } else {
            Emergency e = pq.peek();
            System.out.println("--- NEXT PRIORITY EMERGENCY ---");
            System.out.println("ID: ER" + e.getEmergencyId() + " | Type: " + e.getEmergencyType());
            System.out.println("Severity: " + e.getSeverity() + " | Score: " + e.getPriorityScore());
            System.out.println("Location: " + e.getLocation());
        }
    }

    private static void viewPriorityQueue() {
        PriorityQueue<Emergency> pq = emergencyService.getPriorityQueue();
        if (pq.isEmpty()) {
            System.out.println("⚠️ Priority Queue is empty.");
            return;
        }
        System.out.println("--- CURRENT PRIORITY QUEUE (Highest Priority First) ---");
        PriorityQueue<Emergency> ordered = new PriorityQueue<>(pq);
        while (!ordered.isEmpty()) {
            Emergency e = ordered.poll();
            System.out.println("ER" + e.getEmergencyId() + " | " + e.getSeverity() + " | Score: " + e.getPriorityScore());
        }
    }

    private static void findAmbulance() throws SQLException {
        System.out.println("--- SEARCHING FOR AMBULANCE ---");
        Ambulance amb = resourceService.findBestAmbulance();
        if (amb != null) {
            System.out.println("✅ Recommended: " + amb.getAmbulanceNumber());
            System.out.println("   Type: " + amb.getAmbulanceType() + " | Distance: " + amb.getDistanceKm() + " km");
        } else {
            System.out.println("❌ No available ambulances found.");
        }
    }

    private static void findHospital() throws SQLException {
        System.out.println("--- SEARCHING FOR HOSPITAL ---");
        
        Hospital hosp = resourceService.findBestHospital(true, true);
        if (hosp != null) {
            System.out.println("✅ Recommended: " + hosp.getName());
            System.out.println("   Distance: " + hosp.getDistanceKm() + " km | ICU Beds: " + hosp.getIcuBeds());
            System.out.println("   Trauma: " + (hosp.isTraumaAvailable() ? "Yes" : "No"));
        } else {
            System.out.println("❌ No suitable hospitals found with required capabilities.");
        }
    }

    private static void checkBlood() throws SQLException {
        System.out.println("--- CHECK BLOOD AVAILABILITY ---");
        String bg = InputValidator.getValidBloodGroup();
        List<BloodBank> banks = new dao.BloodBankDAO().getByBloodGroup(bg);
        if (banks.isEmpty()) {
            System.out.println("❌ No blood banks stock " + bg);
        } else {
            System.out.println("✅ Available Banks for " + bg + ":");
            for (BloodBank b : banks) {
                System.out.println("   - " + b.getName() + " (" + b.getUnitsAvailable() + " units) | " + b.getDistanceKm() + " km");
            }
        }
    }

    private static void generateResponsePlan() throws SQLException {
        PriorityQueue<Emergency> pq = emergencyService.getPriorityQueue();
        if (pq.isEmpty()) {
            System.out.println("⚠️ No pending emergencies to plan for.");
            return;
        }
        
        Emergency e = pq.peek();
        System.out.println("--- GENERATING RESPONSE PLAN FOR ER" + e.getEmergencyId() + " ---");
        Patient patient = emergencyService.getPatient(e.getPatientId());
        if (patient == null) {
            System.out.println("❌ The patient record for this emergency is missing.");
            return;
        }
        String details = (String.valueOf(patient.getConditionDescription()) + " " + e.getEmergencyType()).toLowerCase();
        boolean urgent = "SEVERE".equals(patient.getBloodLoss());
        boolean needsTrauma = details.contains("accident") || details.contains("fracture") || details.contains("chest");
        boolean needsICU = "CRITICAL".equals(e.getSeverity());

        Ambulance amb = resourceService.findBestAmbulance();
        Hospital hosp = resourceService.findBestHospital(needsTrauma, needsICU);
        BloodBank bb = resourceService.findMatchingBloodBank(patient.getBloodGroup(), urgent);

        System.out.println("\n===== EMERGENCY RESPONSE PLAN =====");
        System.out.println("Emergency: ER" + e.getEmergencyId() + " | Severity: " + e.getSeverity());
        
        System.out.println("\n🚑 AMBULANCE:");
        if (amb != null) System.out.println(amb.getAmbulanceNumber() + " | " + amb.getDistanceKm() + " km | " + amb.getAmbulanceType());
        else System.out.println("❌ NO AVAILABLE AMBULANCE");

        System.out.println("\n🏥 HOSPITAL:");
        if (hosp != null) System.out.println(hosp.getName() + " | " + hosp.getDistanceKm() + " km | ICU:" + hosp.getIcuBeds());
        else System.out.println("❌ NO SUITABLE HOSPITAL");

        System.out.println("\n🩸 BLOOD BANK:");
        if (bb != null) System.out.println(bb.getName() + " | " + bb.getDistanceKm() + " km | Units: " + bb.getUnitsAvailable());
        else System.out.println("❌ NO MATCHING BLOOD STOCK");

        String responseStatus = resourceService.saveResponsePlan(e, amb, hosp, bb);
        if ("PLANNED".equals(responseStatus)) {
            pq.poll();
        }
        System.out.println("\nREASON: Plan uses the recorded case details, available resources, and distance.");
        if ("PLANNED".equals(responseStatus)) {
            System.out.println("Response record saved; the emergency is now PLANNED.");
        } else {
            System.out.println("Partial response record saved; the emergency remains PENDING until an ambulance and hospital are available.");
        }
        System.out.println("==================================\n");
    }

    private static void viewActiveEmergencies() throws SQLException {
        System.out.println("--- ACTIVE EMERGENCIES ---");
        List<Emergency> list = new dao.EmergencyDAO().getAllPending();
        if (list.isEmpty()) {
            System.out.println("No active emergencies.");
        } else {
            for (Emergency e : list) {
                System.out.println("ER" + e.getEmergencyId() + " | " + e.getSeverity() + " | Score: " + e.getPriorityScore() + " | " + e.getLocation());
            }
        }
    }

    private static void viewResources() throws SQLException {
        System.out.println("--- CURRENT RESOURCES ---");
        
        // Ambulances
        List<Ambulance> ambs = new dao.AmbulanceDAO().getAll();
        System.out.println("\n🚑 AMBULANCES (" + ambs.size() + "):");
        for (Ambulance a : ambs) {
            System.out.println("   " + a.getAmbulanceNumber() + " | " + a.getStatus() + " | " + a.getDistanceKm() + " km");
        }

        // Hospitals
        List<Hospital> hops = new dao.HospitalDAO().getAll();
        System.out.println("\n🏥 HOSPITALS (" + hops.size() + "):");
        for (Hospital h : hops) {
            System.out.println("   " + h.getName() + " | ICU: " + h.getIcuBeds() + " | Trauma: " + (h.isTraumaAvailable() ? "Yes" : "No"));
        }

        // Blood Banks
        List<BloodBank> bbs = new dao.BloodBankDAO().getAll();
        System.out.println("\n🩸 BLOOD BANKS (" + bbs.size() + "):");
        for (BloodBank b : bbs) {
            System.out.println("   " + b.getName() + " | " + b.getBloodGroup() + ": " + b.getUnitsAvailable() + " units");
        }
    }

    private static void completeResponse() throws SQLException {
        int emergencyId = InputValidator.getIntInRange("Emergency ID to complete: ", 1, Integer.MAX_VALUE);
        if (resourceService.completeResponse(emergencyId)) {
            System.out.println("✅ Response completed. The assigned ambulance is now AVAILABLE.");
        } else {
            System.out.println("❌ No planned response with an assigned ambulance was found for ER" + emergencyId + ".");
        }
    }
}
