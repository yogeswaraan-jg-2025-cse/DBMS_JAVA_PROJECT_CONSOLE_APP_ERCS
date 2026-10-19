package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static boolean driverLoaded = false;

    public static Connection getConnection() throws SQLException {
        synchronized (DBConnection.class) {
            try {
                if (!driverLoaded) {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    driverLoaded = true;
                }
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found. Ensure mysql-connector-j-9.7.0.jar is in lib/", e);
            }
        }
        return DriverManager.getConnection(Constants.DB_URL, Constants.DB_USER, Constants.DB_PASSWORD);
    }

    public static void closeConnection() {
        // Each DAO method owns and closes its own connection.
    }
}
