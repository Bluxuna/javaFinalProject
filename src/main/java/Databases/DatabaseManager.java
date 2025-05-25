
package Databases;

import Databases.dbObjects.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=SupermarketDB;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASS = "Bluxuna20050624!";

    // Connection management
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Database connection established.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Database connection failed: " + e.getMessage());
        }
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

    // Employee CRUD operations
    public static boolean createEmployee(Employee employee) {
        String query = "INSERT INTO Employees (FirstName, LastName, RoleID) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setInt(3, employee.getRoleId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error creating employee: " + e.getMessage());
            return false;
        }
    }

    public static Employee getEmployee(int employeeId) {
        String query = "SELECT * FROM Employees WHERE EmployeeID = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Employee(
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getInt("RoleID")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving employee: " + e.getMessage());
        }
        return null;
    }

    // Product CRUD operations
    public static boolean createProduct(Product product) {
        String query = "INSERT INTO Products (Name, CategoryID, Price, QuantityInStock) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, product.getName());
            stmt.setInt(2, product.getCategoryId());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getQuantityInStock());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error creating product: " + e.getMessage());
            return false;
        }
    }

    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Products";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getString("Name"),
                        rs.getInt("CategoryID"),
                        rs.getDouble("Price"),
                        rs.getInt("QuantityInStock")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving products: " + e.getMessage());
        }
        return products;
    }

    // Sale CRUD operations
//    public static boolean createSale(Sale sale) {
//        String query = "INSERT INTO Sales (EmployeeID, TotalAmount, SaleDate) VALUES (?, ?, ?)";
//        try (Connection conn = getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
//
//            stmt.setInt(1, sale.getEmployeeID());
//            stmt.setDouble(2, sale.getTotalAmount());
//            stmt.setTimestamp(3, sale.getSaleDate());
//
//            if (stmt.executeUpdate() > 0) {
//                // Get the generated sale ID
//                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
//                    if (generatedKeys.next()) {
//                        return createSaleItems(generatedKeys.getInt(1), sale.getSaleItems());
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.err.println("Error creating sale: " + e.getMessage());
//        }
//        return false;
//    }

//    private static boolean createSaleItems(int saleId, List<SaleItem> items) {
//        String query = "INSERT INTO SaleItems (SaleID, ProductID, Quantity, Price) VALUES (?, ?, ?, ?)";
//        try (Connection conn = getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//
//            for (SaleItem item : items) {
//                stmt.setInt(1, saleId);
//                stmt.setInt(2, item.getProductId());
//                stmt.setInt(3, item.getQuantity());
//                stmt.setDouble(4, item.getPrice());
//                stmt.addBatch();
//            }
//
//            int[] results = stmt.executeBatch();
//            return results.length == items.size();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.err.println("Error creating sale items: " + e.getMessage());
//            return false;
//        }
//    }

    // Inventory CRUD operations
    public static boolean updateInventory(Inventory inventory) {
        String query = "UPDATE Products SET QuantityInStock = ? WHERE ProductID = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, inventory.getQuantity());
            stmt.setInt(2, inventory.getProductID());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating inventory: " + e.getMessage());
            return false;
        }
    }

    // Shift CRUD operations
    public static boolean createShift(Shift shift) {
        String query = "INSERT INTO Shifts (EmployeeID, StartTime, EndTime) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, shift.getEmployeeID());
            stmt.setTimestamp(2, new Timestamp(shift.getStartTime().getTime()));
            stmt.setTimestamp(3, new Timestamp(shift.getEndTime().getTime()));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error creating shift: " + e.getMessage());
            return false;
        }
    }

    // Generic query methods
    public static ResultSet executeQuery(String query, Object... parameters) {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);

            for (int i = 0; i < parameters.length; i++) {
                stmt.setObject(i + 1, parameters[i]);
            }

            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error executing query: " + e.getMessage());
            return null;
        }
    }
    public static boolean employeeExists(String firstName, String lastName) {
        String query = "SELECT * FROM Employees WHERE FirstName = ? AND LastName = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error checking employee existence: " + e.getMessage());
            return false;
        }
    }




    public static boolean executeUpdate(String query, Object... parameters) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (int i = 0; i < parameters.length; i++) {
                stmt.setObject(i + 1, parameters[i]);
            }

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error executing update: " + e.getMessage());
            return false;
        }
    }
}