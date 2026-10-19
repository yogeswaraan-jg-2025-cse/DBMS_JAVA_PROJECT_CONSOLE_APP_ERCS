package util;

import java.util.Scanner;

public class InputValidator {
    private static Scanner scanner = new Scanner(System.in);

    public static String getNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("❌ Input cannot be empty.");
        }
    }

    public static int getIntInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.println("❌ Value must be between " + min + " and " + max);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid number format.");
            }
        }
    }

    public static String getValidBloodGroup() {
        while (true) {
            String bg = getNonEmptyString("Enter Blood Group (e.g., O+): ").toUpperCase();
            for (String valid : Constants.VALID_BLOOD_GROUPS) {
                if (valid.equals(bg)) return bg;
            }
            System.out.println("❌ Invalid blood group. Valid: A+, A-, B+, B-, AB+, AB-, O+, O-");
        }
    }

    public static String getValidBloodLoss() {
        while (true) {
            String bl = getNonEmptyString("Enter Blood Loss (NONE/MILD/MODERATE/SEVERE): ").toUpperCase();
            for (String valid : Constants.VALID_BLOOD_LOSS) {
                if (valid.equals(bl)) return bl;
            }
            System.out.println("❌ Invalid option. Choose: NONE, MILD, MODERATE, SEVERE");
        }
    }
}