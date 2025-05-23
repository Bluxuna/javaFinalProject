package Databases;

import java.sql.*;

public class DatabaseManager {
    // Database connection parameters (replace with your actual database details)
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=SupermarketDB;encrypt=true;trustServerCertificate=true;";

    private static final String USER = "sa";
    private static final String PASS = "Bluxuna20050624!";

    public static Connection getConnection() {
        Connection conn = null;
        try {

            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Database connection established.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Database connection failed: " + e.getMessage());
        }
        // Catch ClassNotFoundException if using Class.forName()
        // catch (ClassNotFoundException e) {
        //     e.printStackTrace();
        //     System.err.println("JDBC Driver not found: " + e.getMessage());
        // }
        return conn;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    public static boolean employeeExists(String firstName, String lastName) {
        String query = "SELECT COUNT(*) FROM Employees WHERE FirstName = ? AND LastName = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error checking employee existence: " + e.getMessage());
        }
        return false;
    }
}
