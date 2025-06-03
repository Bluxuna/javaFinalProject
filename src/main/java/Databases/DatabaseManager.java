package Databases;

import Databases.dbObjects.*;
import com.sun.source.tree.TryTree;

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
    /**
     * Inserts a new employee into the Employees table in the SupermarketDB database.
     * 
     * @param employee The Employee object containing the data to be inserted
     * @return true if the insertion was successful, false otherwise
     * 
     * Example usage:
     * Employee newEmployee = new Employee("John", "Doe", 2); // 2 for Cashier role
     * newEmployee.setSupermarketId(1); // Set the supermarket ID
     * boolean success = DatabaseManager.createEmployee(newEmployee);
     * if (success) {
     *     System.out.println("Employee created with ID: " + newEmployee.getEmployeeId());
     * }
     */
    public static boolean createEmployee(Employee employee) {
        String query = "INSERT INTO Employees (FirstName, LastName, RoleID, SuperMarketID, Email, passwordHash, HireDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setInt(3, employee.getRoleId());
            stmt.setInt(4, employee.getSupermarketId());
            stmt.setString(5, employee.getEmail());
            stmt.setString(6, employee.getPasswordHash());

            // If hireDate is null, use current date
            if (employee.getHireDate() == null) {
                stmt.setDate(7, new java.sql.Date(System.currentTimeMillis()));
            } else {
                stmt.setDate(7, new java.sql.Date(employee.getHireDate().getTime()));
            }

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int employeeId = generatedKeys.getInt(1);
                        employee.setEmployeeId(employeeId);
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error creating employee: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateEmployee(Employee employee) {
        String query = "UPDATE Employees SET FirstName = ?, LastName = ?, RoleID = ?, SuperMarketID = ?, Email = ?, passwordHash = ?, HireDate = ? WHERE EmployeeID = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setInt(3, employee.getRoleId());
            stmt.setInt(4, employee.getSupermarketId());
            stmt.setString(5, employee.getEmail());
            stmt.setString(6, employee.getPasswordHash());

            // If hireDate is null, use current date
            if (employee.getHireDate() == null) {
                stmt.setDate(7, new java.sql.Date(System.currentTimeMillis()));
            } else {
                stmt.setDate(7, new java.sql.Date(employee.getHireDate().getTime()));
            }

            stmt.setInt(8, employee.getEmployeeId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating employee: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteEmployee(int employeeId) {
        String query = "DELETE FROM Employees WHERE EmployeeID = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, employeeId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error deleting employee: " + e.getMessage());
            return false;
        }
    }

    public static List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM Employees";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("EmployeeID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getInt("RoleID"),
                        rs.getInt("SuperMarketID"),
                        rs.getString("Email"),
                        rs.getString("passwordHash"),
                        rs.getDate("HireDate")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving employees: " + e.getMessage());
        }
        return employees;
    }

    public static Employee getEmployee(int employeeId) {
        String query = "SELECT * FROM Employees WHERE EmployeeID = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Employee(
                        rs.getInt("EmployeeID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getInt("RoleID"),
                        rs.getInt("SuperMarketID"),
                        rs.getString("Email"),
                        rs.getString("passwordHash"),
                        rs.getDate("HireDate")
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
        // Insert into Products table
        String query = "INSERT INTO Products (Name, CategoryID, Price, Barcode) VALUES (?, ?, ?,?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            if (conn == null) {
                System.err.println("Failed to establish database connection");
                return false;
            }

            stmt.setString(1, product.getName());
            stmt.setInt(2, product.getCategoryId());
            stmt.setDouble(3, product.getPrice());
            stmt.setString(4, product.getBarcode());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int productId = generatedKeys.getInt(1);
                        product.setProductId(productId);

                        // Add to inventory for supermarket ID 1 (default)
                        String inventoryQuery = "INSERT INTO Inventory (ProductID, SupermarketID, Quantity, LastUpdated) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
                        try (PreparedStatement invStmt = conn.prepareStatement(inventoryQuery)) {
                            invStmt.setInt(1, productId);
                            invStmt.setInt(2, 1); // Default supermarket ID
                            invStmt.setInt(3, product.getQuantityInStock());

                            // Execute the inventory insert statement
                            invStmt.executeUpdate();
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error creating product: " + e.getMessage());
            // Print more detailed error information
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            return false;
        }
    }

    public static boolean updateProduct(Product product) {
        String query = "UPDATE Products SET Name = ?, CategoryID = ?, Price = ? WHERE ProductID = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, product.getName());
            stmt.setInt(2, product.getCategoryId());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getProductId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                // Update inventory -
                String inventoryQuery = "UPDATE Inventory SET Quantity = ?, LastUpdated = CURRENT_TIMESTAMP WHERE ProductID = ? AND SupermarketID = 1";
                try (PreparedStatement invStmt = conn.prepareStatement(inventoryQuery)) {
                    invStmt.setInt(1, product.getQuantityInStock());
                    invStmt.setInt(2, product.getProductId());
                    invStmt.executeUpdate();
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating product: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteProduct(int productId) {
        // out of date chekcer
        if (!isProductOutOfDate(productId)) {
            System.err.println("Cannot delete product: Product is not out of date");
            return false;
        }

        // Delete from inventory
        String inventoryQuery = "DELETE FROM Inventory WHERE ProductID = ? AND SupermarketID = 1";
        String productQuery = "DELETE FROM Products WHERE ProductID = ?";

        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Delete from inventory
            try (PreparedStatement invStmt = conn.prepareStatement(inventoryQuery)) {
                invStmt.setInt(1, productId);
                invStmt.executeUpdate();
            }

            // Delete from products
            try (PreparedStatement prodStmt = conn.prepareStatement(productQuery)) {
                prodStmt.setInt(1, productId);
                int affectedRows = prodStmt.executeUpdate();

                if (affectedRows > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            System.err.println("Error deleting product: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    closeConnection(conn);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isProductOutOfDate(int productId) {
        String query = "SELECT LastUpdated FROM Inventory WHERE ProductID = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                java.sql.Timestamp lastUpdated = rs.getTimestamp("LastUpdated");
                if (lastUpdated != null) {
                    long thirtyDaysInMillis = 30L * 24 * 60 * 60 * 1000;
                    long currentTimeMillis = System.currentTimeMillis();
                    return (currentTimeMillis - lastUpdated.getTime()) > thirtyDaysInMillis;
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error checking if product is out of date: " + e.getMessage());
            return false;
        }
    }

    public static Product getProductByName(String productName) {
        String query = "SELECT p.*, i.Quantity FROM Products p " +
                      "INNER JOIN Inventory i ON p.ProductID = i.ProductID " +
                      "WHERE p.Name = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, productName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                        rs.getInt("ProductID"),
                        rs.getString("Name"),
                        rs.getInt("CategoryID"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving product by name: " + e.getMessage());
        }

        return null;
    }

    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.*, i.Quantity FROM Products p " +
                      "INNER JOIN Inventory i ON p.ProductID = i.ProductID " +
                      "WHERE i.SupermarketID = 1"; // Default supermarket ID
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("ProductID"),
                        rs.getString("Name"),
                        rs.getInt("CategoryID"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving products: " + e.getMessage());
        }
        return products;
    }

    public static List<Product> getAllProductsAcrossSupermarkets() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.*, i.Quantity, i.SupermarketID FROM Products p " +
                      "INNER JOIN Inventory i ON p.ProductID = i.ProductID";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("ProductID"),
                        rs.getString("Name"),
                        rs.getInt("CategoryID"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving products across supermarkets: " + e.getMessage());
        }
        return products;
    }
    public static Product getProductById(int id, int amount) {
        Product product = null;
        String query = "SELECT p.*, i.Quantity FROM Products p " +
                      "INNER JOIN Inventory i ON p.ProductID = i.ProductID " +
                      "WHERE p.ProductID = ? AND i.Quantity >= ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.setInt(2, amount);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    product = new Product(
                        rs.getInt("ProductID"),
                        rs.getString("Name"),
                        rs.getInt("CategoryID"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving product: " + e.getMessage());
        }

        return product;
    }

    public static List<Product> searchProductsByName(String searchTerm) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.*, i.Quantity FROM Products p " +
                      "INNER JOIN Inventory i ON p.ProductID = i.ProductID " +
                      "WHERE p.Name LIKE ? AND i.Quantity >= 0";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + searchTerm + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(new Product(
                        rs.getInt("ProductID"),
                        rs.getString("Name"),
                        rs.getInt("CategoryID"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error searching products: " + e.getMessage());
        }

        return products;
    }

    // Sale CRUD operations
    public static boolean createSale(Sale sale) {
        String query = "INSERT INTO Sales (EmployeeID, SupermarketID, TotalAmount, SaleDate) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, sale.getEmployeeID());
            stmt.setInt(2, sale.getSupermarketID());
            stmt.setDouble(3, sale.getTotalAmount());
            stmt.setTimestamp(4, sale.getSaleDate());

            if (stmt.executeUpdate() > 0) {
                // Get the generated sale ID
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        sale.setSaleID(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error creating sale: " + e.getMessage());
        }
        return false;
    }

    public static boolean createSaleItem(SaleItem item) {
        String query = "INSERT INTO SaleItems (SaleID, ProductID, Quantity, UnitPrice) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, item.getSaleID());
            stmt.setInt(2, item.getProductID());
            stmt.setInt(3, item.getQuantity());
            stmt.setBigDecimal(4, item.getUnitPrice());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error creating sale item: " + e.getMessage());
            return false;
        }
    }

    // Inventory CRUD operations
    public static boolean updateInventory(Inventory inventory) {
        String query = "UPDATE Inventory SET Quantity = ? WHERE ProductID = ?";
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

    public static List<Inventory> getInventoryForProduct(int productId) {
        List<Inventory> inventoryList = new ArrayList<>();
        String query = "SELECT * FROM Inventory WHERE ProductID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, productId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Inventory inventory = new Inventory();
                    inventory.setInventoryID(rs.getInt("InventoryID"));
                    inventory.setProductID(rs.getInt("ProductID"));
                    inventory.setSupermarketID(rs.getInt("SupermarketID"));
                    inventory.setQuantity(rs.getInt("Quantity"));
                    inventory.setLastUpdated(rs.getTimestamp("LastUpdated"));
                    inventoryList.add(inventory);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving inventory: " + e.getMessage());
        }

        return inventoryList;
    }

    public static Supermarket getSupermarketById(int supermarketId) {
        Supermarket supermarket = null;
        String query = "SELECT * FROM Supermarkets WHERE SupermarketID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, supermarketId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    supermarket = new Supermarket();
                    supermarket.setSupermarketID(rs.getInt("SupermarketID"));
                    supermarket.setName(rs.getString("Name"));
                    supermarket.setAddress(rs.getString("Address"));
                    supermarket.setPhone(rs.getString("Phone"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving supermarket: " + e.getMessage());
        }

        return supermarket;
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

    public static boolean employeeExists(String firstName, String lastName,int marketID) {
        String query = "SELECT * FROM Employees WHERE FirstName = ? AND LastName = ? AND SuperMarketID =? ";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setInt(3,marketID);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error checking employee existence: " + e.getMessage());
            return false;
        }
    }
    public static boolean isAdmin(String firstName, String lastName) {
        String query = "SELECT RoleID FROM Employees WHERE FirstName = ? AND LastName = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int roleID = rs.getInt("RoleID");
                    return roleID == 1;
                }
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error checking admin status: " + e.getMessage());
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
    // Method to search for products sold within a date range
    public static List<Product> searchProductsSoldByDateRange(String searchTerm, Timestamp startDate, Timestamp endDate) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT DISTINCT p.*, i.Quantity FROM Products p " +
                      "INNER JOIN Inventory i ON p.ProductID = i.ProductID " +
                      "INNER JOIN SaleItems si ON p.ProductID = si.ProductID " +
                      "INNER JOIN Sales s ON si.SaleID = s.SaleID " +
                      "WHERE p.Name LIKE ? AND s.SaleDate BETWEEN ? AND ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setTimestamp(2, startDate);
            stmt.setTimestamp(3, endDate);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(new Product(
                        rs.getInt("ProductID"),
                        rs.getString("Name"),
                        rs.getInt("CategoryID"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error searching products by date range: " + e.getMessage());
        }

        return products;
    }

    // date range get all product
    public static List<Product> getAllProductsSoldByDateRange(Timestamp startDate, Timestamp endDate) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT DISTINCT p.*, i.Quantity FROM Products p " +
                      "INNER JOIN Inventory i ON p.ProductID = i.ProductID " +
                      "INNER JOIN SaleItems si ON p.ProductID = si.ProductID " +
                      "INNER JOIN Sales s ON si.SaleID = s.SaleID " +
                      "WHERE s.SaleDate BETWEEN ? AND ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setTimestamp(1, startDate);
            stmt.setTimestamp(2, endDate);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(new Product(
                        rs.getInt("ProductID"),
                        rs.getString("Name"),
                        rs.getInt("CategoryID"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error getting all products sold by date range: " + e.getMessage());
        }

        return products;
    }

    // Employee search via name
    public static List<Employee> searchEmployeesByName(String searchTerm, int supermarketId) {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM Employees WHERE (FirstName LIKE ? OR LastName LIKE ?) AND SuperMarketID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setString(2, "%" + searchTerm + "%");
            stmt.setInt(3, supermarketId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(new Employee(
                        rs.getInt("EmployeeID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getInt("RoleID"),
                        rs.getInt("SuperMarketID"),
                        rs.getString("Email"),
                        rs.getString("passwordHash"),
                        rs.getDate("HireDate")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error searching employees: " + e.getMessage());
        }

        return employees;
    }

    // Category CRUD
    public static List<ProductCategory> getAllCategories() {
        List<ProductCategory> categories = new ArrayList<>();
        String query = "SELECT * FROM ProductCategories";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ProductCategory category = new ProductCategory();
                category.setCategoryID(rs.getInt("CategoryID"));
                category.setCategoryName(rs.getString("CategoryName"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving categories: " + e.getMessage());
        }

        return categories;
    }

    //kategoriis sheqmna
    public static boolean createCategory(String categoryName) {
        String query = "INSERT INTO ProductCategories (CategoryName) VALUES (?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, categoryName);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error creating category: " + e.getMessage());
            return false;
        }
    }
}
