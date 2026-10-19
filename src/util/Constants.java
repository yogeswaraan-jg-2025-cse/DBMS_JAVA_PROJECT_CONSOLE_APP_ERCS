package util;

public class Constants {
    public static final String DB_URL = env("ERCS_DB_URL", "jdbc:mysql://localhost:3306/ercs?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
    public static final String DB_USER = env("ERCS_DB_USER", "root");
    public static final String DB_PASSWORD = env("ERCS_DB_PASSWORD", "");
    
    public static final String[] VALID_BLOOD_GROUPS = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
    public static final String[] VALID_BLOOD_LOSS = {"NONE", "MILD", "MODERATE", "SEVERE"};

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.trim().isEmpty() ? fallback : value.trim();
    }
}
