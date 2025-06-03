package GUI;

import Databases.DatabaseManager;
import Databases.dbObjects.Employee;
import Databases.dbObjects.Inventory;
import Databases.dbObjects.Product;
import Databases.dbObjects.ProductCategory;
import Databases.dbObjects.Supermarket;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class AdminForm extends Application {
    private Stage primaryStage;
    private VBox root;
    private Button logoutButton;

    // Product search components
    private TextField productSearchField;
    private Button productSearchButton;
    private Button dateRangeSearchButton;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;

    // All products sold between dates components
    private DatePicker allProductsStartDatePicker;
    private DatePicker allProductsEndDatePicker;
    private Button allProductsSoldButton;

    // All products button
    private Button allProductsButton;

    // Employee search components
    private TextField employeeSearchField;
    private ComboBox<Integer> supermarketComboBox;
    private Button employeeSearchButton;

    // Employee CRUD components
    private TabPane employeeCrudTabPane;

    // Add Employee components
    private TextField addEmployeeFirstNameField;
    private TextField addEmployeeLastNameField;
    private ComboBox<String> addEmployeeRoleComboBox;
    private ComboBox<Integer> addEmployeeSupermarketComboBox;
    private TextField addEmployeeEmailField;
    private TextField addEmployeePasswordField;
    private DatePicker addEmployeeHireDatePicker;
    private Button addEmployeeButton;

    // Update Employee components
    private TextField updateEmployeeSearchField;
    private ComboBox<Integer> updateEmployeeSupermarketSearchComboBox;
    private Button updateEmployeeSearchButton;
    private TextField updateEmployeeFirstNameField;
    private TextField updateEmployeeLastNameField;
    private ComboBox<String> updateEmployeeRoleComboBox;
    private ComboBox<Integer> updateEmployeeSupermarketComboBox;
    private TextField updateEmployeeEmailField;
    private TextField updateEmployeePasswordField;
    private DatePicker updateEmployeeHireDatePicker;
    private Button updateEmployeeButton;
    private Employee currentEmployeeForUpdate;

    // Delete Employee components
    private TextField deleteEmployeeSearchField;
    private ComboBox<Integer> deleteEmployeeSupermarketComboBox;
    private Button deleteEmployeeSearchButton;
    private ListView<String> deleteEmployeeListView;
    private Button deleteEmployeeButton;
    private List<Employee> employeesToDelete;

    // Product crud components
    private TabPane productCrudTabPane;

    // Add Product components
    private TextField addProductNameField;
    private ComboBox<String> addProductCategoryComboBox;
    private TextField addProductPriceField;
    private TextField addProductQuantityField;
    private Button addProductButton;

    // Update Product components
    private TextField updateProductSearchField;
    private Button updateProductSearchButton;
    private TextField updateProductNameField;
    private ComboBox<String> updateProductCategoryComboBox;
    private TextField updateProductPriceField;
    private TextField updateProductQuantityField;
    private Button updateProductButton;

    // Delete Product components
    private TextField deleteProductSearchField;
    private Button deleteProductSearchButton;
    private ListView<String> deleteProductListView;
    private Button deleteProductButton;

    // Shared components
    private TextArea resultArea;
    private TabPane tabPane;
    private Map<Integer, String> categoryNames;
    private Product currentProductForUpdate;

    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        stage.setTitle("Admin Panel");

        // Load categories from database
        categoryNames = new HashMap<>();
        loadCategoriesFromDatabase();

        // ful screen
        javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        this.root = new VBox(20);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Admin Panel");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        //  logout button
        logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #ff5555; -fx-text-fill: white;");
        logoutButton.setPrefWidth(100);

        //  HBox for title and logout button
        HBox headerBox = new HBox(20);
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        headerBox.getChildren().addAll(titleLabel, logoutButton);

        //  TabPane
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Create Product Tab
        Tab productTab = new Tab("Product managing");
        VBox productVBox = new VBox(20);
        productVBox.setPadding(new Insets(20));

        // Product crud operations
        this.productCrudTabPane = new TabPane();
        productCrudTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // searc Product Tab
        Tab searchProductTab = new Tab("Search Product");
        VBox searchProductVBox = new VBox(20);
        searchProductVBox.setPadding(new Insets(10));

        // bsic product search
        Label productSearchLabel = new Label("Enter product name:");
        productSearchField = new TextField();
        productSearchField.setPromptText("Product name");
        productSearchField.setPrefWidth(300);
        productSearchButton = new Button("Search");
        productSearchButton.setPrefWidth(100);

        HBox productSearchBox = new HBox(10);
        productSearchBox.getChildren().addAll(productSearchField, productSearchButton);

        // Date range search
        Label dateRangeLabel = new Label("Search sold products  between dates:");

        startDatePicker = new DatePicker(LocalDate.now().minusMonths(1));
        startDatePicker.setPromptText("Start Date");

        endDatePicker = new DatePicker(LocalDate.now());
        endDatePicker.setPromptText("End Date");

        dateRangeSearchButton = new Button("Search by Date Range");
        dateRangeSearchButton.setPrefWidth(150);

        HBox dateRangeBox = new HBox(10);
        dateRangeBox.getChildren().addAll(startDatePicker, endDatePicker, dateRangeSearchButton);

        // All products sold between dates section
        Label allProductsSoldLabel = new Label("Get all products sold between dates:");

        allProductsStartDatePicker = new DatePicker(LocalDate.now().minusMonths(1));
        allProductsStartDatePicker.setPromptText("Start Date");

        allProductsEndDatePicker = new DatePicker(LocalDate.now());
        allProductsEndDatePicker.setPromptText("End Date");

        allProductsSoldButton = new Button("Get All Sold Products");
        allProductsSoldButton.setPrefWidth(150);

        HBox allProductsSoldBox = new HBox(10);
        allProductsSoldBox.getChildren().addAll(allProductsStartDatePicker, allProductsEndDatePicker, allProductsSoldButton);

        // All products section
        Label allProductsLabel = new Label("Get all products in the system:");

        allProductsButton = new Button("Get All Products");
        allProductsButton.setPrefWidth(150);

        HBox allProductsBox = new HBox(10);
        allProductsBox.getChildren().add(allProductsButton);

        searchProductVBox.getChildren().addAll(
            productSearchLabel, productSearchBox,
            dateRangeLabel, dateRangeBox,
            allProductsSoldLabel, allProductsSoldBox,
            allProductsLabel, allProductsBox
        );
        searchProductTab.setContent(searchProductVBox);

        // Add Product Tab
        Tab addProductTab = new Tab("Add Product");
        VBox addProductVBox = new VBox(20);
        addProductVBox.setPadding(new Insets(10));

        Label addProductNameLabel = new Label("Product Name:");
        this.addProductNameField = new TextField();
        this.addProductNameField.setPromptText("Enter product name");

        Label addProductCategoryLabel = new Label("Category:");
        this.addProductCategoryComboBox = new ComboBox<>();
        updateCategoryComboBoxes();
        this.addProductCategoryComboBox.setPromptText("Select category");

        Label addProductPriceLabel = new Label("Price:");
        this.addProductPriceField = new TextField();
        this.addProductPriceField.setPromptText("Enter price");

        Label addProductQuantityLabel = new Label("Quantity:");
        this.addProductQuantityField = new TextField();
        this.addProductQuantityField.setPromptText("Enter quantity");

        this.addProductButton = new Button("Add Product");
        this.addProductButton.setPrefWidth(150);

        addProductVBox.getChildren().addAll(
            addProductNameLabel, this.addProductNameField,
            addProductCategoryLabel, this.addProductCategoryComboBox,
            addProductPriceLabel, this.addProductPriceField,
            addProductQuantityLabel, this.addProductQuantityField,
            this.addProductButton
        );
        addProductTab.setContent(addProductVBox);

        // Update Product Tab
        Tab updateProductTab = new Tab("Update Product");
        VBox updateProductVBox = new VBox(20);
        updateProductVBox.setPadding(new Insets(10));

        Label updateProductSearchLabel = new Label("Search Product to Update:");
        TextField updateProductSearchField = new TextField();
        updateProductSearchField.setPromptText("Enter product name");
        Button updateProductSearchButton = new Button("Search");
        updateProductSearchButton.setPrefWidth(100);

        HBox updateProductSearchBox = new HBox(10);
        updateProductSearchBox.getChildren().addAll(updateProductSearchField, updateProductSearchButton);

        Label updateProductNameLabel = new Label("Product Name:");
        TextField updateProductNameField = new TextField();
        updateProductNameField.setPromptText("Enter new product name");

        Label updateProductCategoryLabel = new Label("Category:");
        ComboBox<String> updateProductCategoryComboBox = new ComboBox<>();
        // Categories will be loaded in updateCategoryComboBoxes method
        updateProductCategoryComboBox.setPromptText("Select new category");

        Label updateProductPriceLabel = new Label("Price:");
        TextField updateProductPriceField = new TextField();
        updateProductPriceField.setPromptText("Enter new price");

        Label updateProductQuantityLabel = new Label("Quantity:");
        TextField updateProductQuantityField = new TextField();
        updateProductQuantityField.setPromptText("Enter new quantity");

        Button updateProductButton = new Button("Update Product");
        updateProductButton.setPrefWidth(150);
        updateProductButton.setDisable(true);

        updateProductVBox.getChildren().addAll(
            updateProductSearchLabel, updateProductSearchBox,
            updateProductNameLabel, updateProductNameField,
            updateProductCategoryLabel, updateProductCategoryComboBox,
            updateProductPriceLabel, updateProductPriceField,
            updateProductQuantityLabel, updateProductQuantityField,
            updateProductButton
        );
        updateProductTab.setContent(updateProductVBox);

        // Delete Product Tab
        Tab deleteProductTab = new Tab("Delete Product");
        VBox deleteProductVBox = new VBox(20);
        deleteProductVBox.setPadding(new Insets(10));

        Label deleteProductSearchLabel = new Label("Search Product to Delete (Only out-of-date products can be deleted):");
        TextField deleteProductSearchField = new TextField();
        deleteProductSearchField.setPromptText("Enter product name");
        Button deleteProductSearchButton = new Button("Search");
        deleteProductSearchButton.setPrefWidth(100);

        HBox deleteProductSearchBox = new HBox(10);
        deleteProductSearchBox.getChildren().addAll(deleteProductSearchField, deleteProductSearchButton);

        ListView<String> deleteProductListView = new ListView<>();
        deleteProductListView.setPrefHeight(200);

        Button deleteProductButton = new Button("Delete Selected Product");
        deleteProductButton.setPrefWidth(200);
        deleteProductButton.setDisable(true);

        deleteProductVBox.getChildren().addAll(
            deleteProductSearchLabel, deleteProductSearchBox,
            deleteProductListView, deleteProductButton
        );
        deleteProductTab.setContent(deleteProductVBox);

        // Add all tabs to the product  tab pane
        productCrudTabPane.getTabs().addAll(searchProductTab, addProductTab, updateProductTab, deleteProductTab);

        productVBox.getChildren().add(productCrudTabPane);

        // Wrap productVBox in a ScrollPane for better scrolling on smaller screens
        ScrollPane productScrollPane = new ScrollPane(productVBox);
        productScrollPane.setFitToWidth(true);
        productScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        productScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        productTab.setContent(productScrollPane);
//-------------------------------------------------------------------------------------------------
        // Create Employee Tab
        Tab employeeTab = new Tab("Employee managing");
        VBox employeeVBox = new VBox(20);
        employeeVBox.setPadding(new Insets(20));

        // Employee CRUD operations
        this.employeeCrudTabPane = new TabPane();
        employeeCrudTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Search Employee Tab
        Tab searchEmployeeTab = new Tab("Search Employee");
        VBox searchEmployeeVBox = new VBox(20);
        searchEmployeeVBox.setPadding(new Insets(10));

        // Employee search
        Label employeeSearchLabel = new Label("Enter employee name to search:");
        employeeSearchField = new TextField();
        employeeSearchField.setPromptText("Employee name");
        employeeSearchField.setPrefWidth(300);

        // Supermarket selection
        Label supermarketLabel = new Label("Select supermarket:");
        supermarketComboBox = new ComboBox<>();
        supermarketComboBox.setPromptText("Select supermarket");
        supermarketComboBox.setPrefWidth(200);

        // Populate supermarket combo box (in a real app, these would come from the database)
        supermarketComboBox.getItems().addAll(1, 2, 3); // Example supermarket IDs
        supermarketComboBox.setValue(1); // Default selection

        employeeSearchButton = new Button("Search");
        employeeSearchButton.setPrefWidth(100);

        HBox employeeSearchBox = new HBox(10);
        employeeSearchBox.getChildren().addAll(employeeSearchField, supermarketComboBox, employeeSearchButton);

        searchEmployeeVBox.getChildren().addAll(
            employeeSearchLabel,
            employeeSearchBox
        );
        searchEmployeeTab.setContent(searchEmployeeVBox);

        // Add Employee Tab
        Tab addEmployeeTab = new Tab("Add Employee");
        VBox addEmployeeVBox = new VBox(20);
        addEmployeeVBox.setPadding(new Insets(10));

        Label addEmployeeFirstNameLabel = new Label("First Name:");
        this.addEmployeeFirstNameField = new TextField();
        this.addEmployeeFirstNameField.setPromptText("Enter first name");

        Label addEmployeeLastNameLabel = new Label("Last Name:");
        this.addEmployeeLastNameField = new TextField();
        this.addEmployeeLastNameField.setPromptText("Enter last name");

        Label addEmployeeRoleLabel = new Label("Role:");
        this.addEmployeeRoleComboBox = new ComboBox<>();
        this.addEmployeeRoleComboBox.getItems().addAll("Admin", "Cashier");
        this.addEmployeeRoleComboBox.setPromptText("Select role");

        Label addEmployeeSupermarketLabel = new Label("Supermarket:");
        this.addEmployeeSupermarketComboBox = new ComboBox<>();
        this.addEmployeeSupermarketComboBox.getItems().addAll(1, 2, 3); // Example supermarket IDs
        this.addEmployeeSupermarketComboBox.setPromptText("Select supermarket");

        this.addEmployeeButton = new Button("Add Employee");
        this.addEmployeeButton.setPrefWidth(150);

        // Email field
        Label addEmployeeEmailLabel = new Label("Email:");
        this.addEmployeeEmailField = new TextField();
        this.addEmployeeEmailField.setPromptText("Enter email");

        // Password field
        Label addEmployeePasswordLabel = new Label("Password:");
        this.addEmployeePasswordField = new TextField();
        this.addEmployeePasswordField.setPromptText("Enter password");

        // Hire Date field
        Label addEmployeeHireDateLabel = new Label("Hire Date:");
        this.addEmployeeHireDatePicker = new DatePicker(java.time.LocalDate.now());
        this.addEmployeeHireDatePicker.setPromptText("Select hire date");

        addEmployeeVBox.getChildren().addAll(
            addEmployeeFirstNameLabel, addEmployeeFirstNameField,
            addEmployeeLastNameLabel, addEmployeeLastNameField,
            addEmployeeRoleLabel, addEmployeeRoleComboBox,
            addEmployeeSupermarketLabel, addEmployeeSupermarketComboBox,
            addEmployeeEmailLabel, addEmployeeEmailField,
            addEmployeePasswordLabel, addEmployeePasswordField,
            addEmployeeHireDateLabel, addEmployeeHireDatePicker,
            addEmployeeButton
        );
        addEmployeeTab.setContent(addEmployeeVBox);

        // Update Employee Tab
        Tab updateEmployeeTab = new Tab("Update Employee");
        VBox updateEmployeeVBox = new VBox(20);
        updateEmployeeVBox.setPadding(new Insets(10));

        Label updateEmployeeSearchLabel = new Label("Search Employee to Update:");
        this.updateEmployeeSearchField = new TextField();
        this.updateEmployeeSearchField.setPromptText("Enter employee name");

        Label updateEmployeeSupermarketSearchLabel = new Label("Supermarket:");
        this.updateEmployeeSupermarketSearchComboBox = new ComboBox<>();
        this.updateEmployeeSupermarketSearchComboBox.getItems().addAll(1, 2, 3); // Example supermarket IDs
        this.updateEmployeeSupermarketSearchComboBox.setValue(1); // Default selection
        this.updateEmployeeSupermarketSearchComboBox.setPromptText("Select supermarket");

        this.updateEmployeeSearchButton = new Button("Search");
        this.updateEmployeeSearchButton.setPrefWidth(100);

        HBox updateEmployeeSearchBox = new HBox(10);
        updateEmployeeSearchBox.getChildren().addAll(this.updateEmployeeSearchField, this.updateEmployeeSupermarketSearchComboBox, this.updateEmployeeSearchButton);

        Label updateEmployeeFirstNameLabel = new Label("First Name:");
        this.updateEmployeeFirstNameField = new TextField();
        this.updateEmployeeFirstNameField.setPromptText("Enter new first name");

        Label updateEmployeeLastNameLabel = new Label("Last Name:");
        this.updateEmployeeLastNameField = new TextField();
        this.updateEmployeeLastNameField.setPromptText("Enter new last name");

        Label updateEmployeeRoleLabel = new Label("Role:");
        this.updateEmployeeRoleComboBox = new ComboBox<>();
        this.updateEmployeeRoleComboBox.getItems().addAll("Admin", "Cashier");
        this.updateEmployeeRoleComboBox.setPromptText("Select new role");

        Label updateEmployeeSupermarketLabel = new Label("Supermarket:");
        this.updateEmployeeSupermarketComboBox = new ComboBox<>();
        this.updateEmployeeSupermarketComboBox.getItems().addAll(1, 2, 3); // Example supermarket IDs
        this.updateEmployeeSupermarketComboBox.setPromptText("Select new supermarket");

        this.updateEmployeeButton = new Button("Update Employee");
        this.updateEmployeeButton.setPrefWidth(150);
        this.updateEmployeeButton.setDisable(true);

        // Email field
        Label updateEmployeeEmailLabel = new Label("Email:");
        this.updateEmployeeEmailField = new TextField();
        this.updateEmployeeEmailField.setPromptText("Enter email");

        // Password field
        Label updateEmployeePasswordLabel = new Label("Password:");
        this.updateEmployeePasswordField = new TextField();
        this.updateEmployeePasswordField.setPromptText("Enter password");

        // Hire Date field
        Label updateEmployeeHireDateLabel = new Label("Hire Date:");
        this.updateEmployeeHireDatePicker = new DatePicker();
        this.updateEmployeeHireDatePicker.setPromptText("Select hire date");

        updateEmployeeVBox.getChildren().addAll(
            updateEmployeeSearchLabel, updateEmployeeSearchBox,
            updateEmployeeFirstNameLabel, updateEmployeeFirstNameField,
            updateEmployeeLastNameLabel, updateEmployeeLastNameField,
            updateEmployeeRoleLabel, updateEmployeeRoleComboBox,
            updateEmployeeSupermarketLabel, updateEmployeeSupermarketComboBox,
            updateEmployeeEmailLabel, updateEmployeeEmailField,
            updateEmployeePasswordLabel, updateEmployeePasswordField,
            updateEmployeeHireDateLabel, updateEmployeeHireDatePicker,
            updateEmployeeButton
        );
        updateEmployeeTab.setContent(updateEmployeeVBox);

        // Delete Employee Tab
        Tab deleteEmployeeTab = new Tab("Delete Employee");
        VBox deleteEmployeeVBox = new VBox(20);
        deleteEmployeeVBox.setPadding(new Insets(10));

        Label deleteEmployeeSearchLabel = new Label("Search Employee to Delete:");
        this.deleteEmployeeSearchField = new TextField();
        this.deleteEmployeeSearchField.setPromptText("Enter employee name");

        Label deleteEmployeeSupermarketLabel = new Label("Supermarket:");
        this.deleteEmployeeSupermarketComboBox = new ComboBox<>();
        this.deleteEmployeeSupermarketComboBox.getItems().addAll(1, 2, 3); // Example supermarket IDs
        this.deleteEmployeeSupermarketComboBox.setValue(1); // Default selection
        this.deleteEmployeeSupermarketComboBox.setPromptText("Select supermarket");

        this.deleteEmployeeSearchButton = new Button("Search");
        this.deleteEmployeeSearchButton.setPrefWidth(100);

        HBox deleteEmployeeSearchBox = new HBox(10);
        deleteEmployeeSearchBox.getChildren().addAll(this.deleteEmployeeSearchField, this.deleteEmployeeSupermarketComboBox, this.deleteEmployeeSearchButton);

        this.deleteEmployeeListView = new ListView<>();
        this.deleteEmployeeListView.setPrefHeight(200);

        this.deleteEmployeeButton = new Button("Delete Selected Employee");
        this.deleteEmployeeButton.setPrefWidth(200);
        this.deleteEmployeeButton.setDisable(true);

        // list to store employees to delete
        this.employeesToDelete = new ArrayList<>();

        deleteEmployeeVBox.getChildren().addAll(
            deleteEmployeeSearchLabel, deleteEmployeeSearchBox,
            deleteEmployeeListView, deleteEmployeeButton
        );
        deleteEmployeeTab.setContent(deleteEmployeeVBox);

        // Add all tabs to the employee tab pane
        employeeCrudTabPane.getTabs().addAll(searchEmployeeTab, addEmployeeTab, updateEmployeeTab, deleteEmployeeTab);

        employeeVBox.getChildren().add(employeeCrudTabPane);

        // Wrap employeeVBox in a ScrollPane for better scrolling on smaller screens
        ScrollPane employeeScrollPane = new ScrollPane(employeeVBox);
        employeeScrollPane.setFitToWidth(true);
        employeeScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        employeeScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        employeeTab.setContent(employeeScrollPane);

        // Create Category Management Tab
        Tab categoryTab = new Tab("Category Management");
        VBox categoryVBox = new VBox(20);
        categoryVBox.setPadding(new Insets(20));

        Label categoryTitleLabel = new Label("Add New Category");
        categoryTitleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label categoryNameLabel = new Label("Category Name:");
        TextField categoryNameField = new TextField();
        categoryNameField.setPromptText("Enter new category name");

        Button addCategoryButton = new Button("Add Category");
        addCategoryButton.setPrefWidth(150);

        ListView<String> categoriesListView = new ListView<>();
        categoriesListView.setPrefHeight(300);
        updateCategoriesListView(categoriesListView);

        Label existingCategoriesLabel = new Label("Existing Categories:");
        existingCategoriesLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        categoryVBox.getChildren().addAll(
            categoryTitleLabel,
            categoryNameLabel, categoryNameField,
            addCategoryButton,
            existingCategoriesLabel,
            categoriesListView
        );

        // Set action handler for add category button
        addCategoryButton.setOnAction(e -> {
            addCategory(categoryNameField, categoriesListView);
        });

        // Wrap categoryVBox in a ScrollPane for better scrolling on smaller screens
        ScrollPane categoryScrollPane = new ScrollPane(categoryVBox);
        categoryScrollPane.setFitToWidth(true);
        categoryScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        categoryScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        categoryTab.setContent(categoryScrollPane);

        // Add tabs to TabPane
        tabPane.getTabs().addAll(productTab, employeeTab, categoryTab);

        // Wrap tabPane in a ScrollPane for better scrolling on smaller screens
        ScrollPane tabScrollPane = new ScrollPane(tabPane);
        tabScrollPane.setFitToWidth(true);
        tabScrollPane.setFitToHeight(true);
        tabScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        tabScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        tabScrollPane.setPrefHeight(screenBounds.getHeight() * 0.6); // Adjust height to take 60% of screen
        tabScrollPane.setMaxHeight(Double.MAX_VALUE);

        // Result area
        resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefHeight(screenBounds.getHeight() * 0.3); // Adjust to take 30% of screen
        resultArea.setWrapText(true);
        resultArea.setMaxWidth(Double.MAX_VALUE);
        resultArea.setMaxHeight(Double.MAX_VALUE);

        // Wrap resultArea in a ScrollPane for better scrolling during CRUD operations
        ScrollPane resultScrollPane = new ScrollPane(resultArea);
        resultScrollPane.setFitToWidth(true);
        resultScrollPane.setFitToHeight(true);
        resultScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        resultScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        resultScrollPane.setPrefHeight(screenBounds.getHeight() * 0.3); // Adjust to take 30% of screen

        root.getChildren().addAll(
            headerBox,
            tabScrollPane,
            resultScrollPane
        );

        // Set action handlers for product search
        productSearchButton.setOnAction(e -> searchProduct());
        dateRangeSearchButton.setOnAction(e -> searchProductByDateRange());
        allProductsSoldButton.setOnAction(e -> getAllProductsSoldByDateRange());
        allProductsButton.setOnAction(e -> getAllProducts());

        // Add product components are already initialized

        // Set action handler for add product
        this.addProductButton.setOnAction(e -> addProduct());

        // Get references to the update product components
        this.updateProductSearchField = (TextField) ((HBox) ((VBox) ((Tab) productCrudTabPane.getTabs().get(2)).getContent()).getChildren().get(1)).getChildren().get(0);
        this.updateProductSearchButton = (Button) ((HBox) ((VBox) ((Tab) productCrudTabPane.getTabs().get(2)).getContent()).getChildren().get(1)).getChildren().get(1);
        this.updateProductNameField = (TextField) ((VBox) ((Tab) productCrudTabPane.getTabs().get(2)).getContent()).getChildren().get(3);
        this.updateProductCategoryComboBox = (ComboBox<String>) ((VBox) ((Tab) productCrudTabPane.getTabs().get(2)).getContent()).getChildren().get(5);
        this.updateProductPriceField = (TextField) ((VBox) ((Tab) productCrudTabPane.getTabs().get(2)).getContent()).getChildren().get(7);
        this.updateProductQuantityField = (TextField) ((VBox) ((Tab) productCrudTabPane.getTabs().get(2)).getContent()).getChildren().get(9);
        this.updateProductButton = (Button) ((VBox) ((Tab) productCrudTabPane.getTabs().get(2)).getContent()).getChildren().get(10);

        // Set action handlers for update product
        this.updateProductSearchButton.setOnAction(e -> searchProductToUpdate());
        this.updateProductButton.setOnAction(e -> updateProduct());

        // Get references to the delete product components
        this.deleteProductSearchField = (TextField) ((HBox) ((VBox) ((Tab) productCrudTabPane.getTabs().get(3)).getContent()).getChildren().get(1)).getChildren().get(0);
        this.deleteProductSearchButton = (Button) ((HBox) ((VBox) ((Tab) productCrudTabPane.getTabs().get(3)).getContent()).getChildren().get(1)).getChildren().get(1);
        this.deleteProductListView = (ListView<String>) ((VBox) ((Tab) productCrudTabPane.getTabs().get(3)).getContent()).getChildren().get(2);
        this.deleteProductButton = (Button) ((VBox) ((Tab) productCrudTabPane.getTabs().get(3)).getContent()).getChildren().get(3);

        // Set action handlers for delete product
        this.deleteProductSearchButton.setOnAction(e -> searchProductToDelete());
        this.deleteProductButton.setOnAction(e -> deleteProduct());

        // Set action handler for employee search
        employeeSearchButton.setOnAction(e -> searchEmployees());

        // Set action handlers for employee CRUD operations
        addEmployeeButton.setOnAction(e -> addEmployee());
        updateEmployeeSearchButton.setOnAction(e -> searchEmployeeToUpdate());
        updateEmployeeButton.setOnAction(e -> updateEmployee());
        deleteEmployeeSearchButton.setOnAction(e -> searchEmployeeToDelete());
        deleteEmployeeButton.setOnAction(e -> deleteEmployee());

        // Set action handler for logout button
        logoutButton.setOnAction(e -> handleLogout());

        // Create a full screen scene
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
        stage.setTitle("Admin Search");
        stage.setScene(scene);
        stage.setMaximized(true); // Make sure it's maximized
        stage.show();
    }

    private void searchProduct() {
        String searchTerm = productSearchField.getText().trim();
        resultArea.clear();

        if (searchTerm.isEmpty()) {
            resultArea.setText("Please enter a product name to search");
            return;
        }

        List<Product> products = DatabaseManager.searchProductsByName(searchTerm);
        displayProductResults(products, "Search Results for: " + searchTerm);
    }

    private void searchProductByDateRange() {
        String searchTerm = productSearchField.getText().trim();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        resultArea.clear();

        if (searchTerm.isEmpty()) {
            resultArea.setText("Please enter a product name");
            return;
        }

        if (startDate == null || endDate == null) {
            resultArea.setText("Please select both start and end dates");
            return;
        }

        if (endDate.isBefore(startDate)) {
            resultArea.setText("End date cannot be before start date");
            return;
        }

        //  LocalDate to Timestamp  database
        Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(startDate, LocalTime.MIDNIGHT));
        Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(endDate, LocalTime.MAX));

        List<Product> products = DatabaseManager.searchProductsSoldByDateRange(searchTerm, startTimestamp, endTimestamp);
        displayProductResults(products, "Search Results for: " + searchTerm + " (Sold between " + startDate + " and " + endDate + ")");
    }

    private void getAllProductsSoldByDateRange() {
        LocalDate startDate = allProductsStartDatePicker.getValue();
        LocalDate endDate = allProductsEndDatePicker.getValue();
        resultArea.clear();

        if (startDate == null || endDate == null) {
            resultArea.setText("Please select both start and end dates");
            return;
        }

        if (endDate.isBefore(startDate)) {
            resultArea.setText("End date cannot be before start date");
            return;
        }

        // Convert LocalDate to Timestamp for database query
        Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(startDate, LocalTime.MIDNIGHT));
        Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(endDate, LocalTime.MAX));

        List<Product> products = DatabaseManager.getAllProductsSoldByDateRange(startTimestamp, endTimestamp);
        displayProductResults(products, "All Products Sold Between " + startDate + " and " + endDate);
    }

    private void getAllProducts() {
        resultArea.clear();

        List<Product> products = DatabaseManager.getAllProducts();
        displayProductResults(products, "All Products in the System");
    }

    private void searchEmployees() {
        String searchTerm = employeeSearchField.getText().trim();
        int supermarketId = supermarketComboBox.getValue();
        resultArea.clear();

        if (searchTerm.isEmpty()) {
            resultArea.setText("Please enter an employee name to search");
            return;
        }

        List<Employee> employees = DatabaseManager.searchEmployeesByName(searchTerm, supermarketId);

        if (employees.isEmpty()) {
            resultArea.setText("No employees found matching: " + searchTerm + " in supermarket ID: " + supermarketId);
        } else {
            StringBuilder result = new StringBuilder();
            result.append("Search Results for: ").append(searchTerm).append(" in supermarket ID: ").append(supermarketId).append("\n");
            result.append("=".repeat(50)).append("\n\n");

            for (Employee employee : employees) {
                result.append("EMPLOYEE DETAILS\n");
                result.append("-".repeat(30)).append("\n");
                result.append("Employee ID: ").append(employee.getEmployeeId()).append("\n");
                result.append("Name: ").append(employee.getFirstName()).append(" ").append(employee.getLastName()).append("\n");

                // Get role name based on role ID
                String roleName;
                switch (employee.getRoleId()) {
                    case 1:
                        roleName = "Admin";
                        break;
                    case 2:
                        roleName = "Cashier";
                        break;
                    default:
                        roleName = "Unknown Role";
                }

                result.append("Role: ").append(roleName).append(" (ID: ").append(employee.getRoleId()).append(")\n");
                result.append("Supermarket ID: ").append(employee.getSupermarketId()).append("\n");

                // Display email if available
                if (employee.getEmail() != null && !employee.getEmail().isEmpty()) {
                    result.append("Email: ").append(employee.getEmail()).append("\n");
                }

                // Display hire date if available
                if (employee.getHireDate() != null) {
                    result.append("Hire Date: ").append(employee.getHireDate()).append("\n");
                }

                result.append("\n");
                result.append("=".repeat(50)).append("\n\n");
            }

            resultArea.setText(result.toString());
        }
    }


    private void displayProductResults(List<Product> products, String headerText) {
        if (products.isEmpty()) {
            resultArea.setText("No products found matching your search criteria");
        } else {
            StringBuilder result = new StringBuilder();
            result.append(headerText).append("\n");
            result.append("=".repeat(50)).append("\n\n");

            for (Product product : products) {
                // Display basic product information
                result.append("PRODUCT DETAILS\n");
                result.append("-".repeat(30)).append("\n");
                result.append("Product ID: ").append(product.getProductId()).append("\n");
                result.append("Name: ").append(product.getName()).append("\n");
                result.append("Price: ").append(String.format("%.2f", product.getPrice())).append("\n");

                // category name
                String categoryName = categoryNames.getOrDefault(product.getCategoryId(), "Unknown Category");
                result.append("category: ").append(categoryName).append(" (ID: ").append(product.getCategoryId()).append(")\n");
                result.append("amount: ").append(product.getQuantityInStock()).append("\n\n");


                result.append("teritoriulad sadaa produqti\n");
                result.append("-".repeat(30)).append("\n");
                List<Inventory> inventoryList = DatabaseManager.getInventoryForProduct(product.getProductId());

                if (inventoryList.isEmpty()) {
                    result.append("No inventory information available for this product.\n\n");
                } else {
                    for (Inventory inventory : inventoryList) {
                        Supermarket supermarket = DatabaseManager.getSupermarketById(inventory.getSupermarketID());
                        if (supermarket != null) {
                            result.append("Store: ").append(supermarket.getName()).append("\n");
                            result.append("Address: ").append(supermarket.getAddress()).append("\n");
                            result.append("wilifon: ").append(supermarket.getPhone()).append("\n");
                            result.append("Quantity : ").append(inventory.getQuantity()).append("\n");
                            result.append("Last Updated: ").append(inventory.getLastUpdated()).append("\n");

                            // Check if product is out of date
                            boolean isOutOfDate = DatabaseManager.isProductOutOfDate(product.getProductId());
                            result.append("Out of Date: ").append(isOutOfDate ? "Yes" : "No").append("\n\n");
                        }
                    }
                }

                result.append("=".repeat(50)).append("\n\n");
            }
            resultArea.setText(result.toString());
        }
    }

    // Method to add a new product
    private void addProduct() {
        String name = addProductNameField.getText().trim();
        String categoryStr = addProductCategoryComboBox.getValue();
        String priceStr = addProductPriceField.getText().trim();
        String quantityStr = addProductQuantityField.getText().trim();

        // Validate inputs
        if (name.isEmpty() || categoryStr == null || priceStr.isEmpty() || quantityStr.isEmpty()) {
            resultArea.setText("sheavsee yvelaa!!");
            return;
        }

        try {
            // Convert category name to ID
            int categoryId = 1; // Default to Groceries
            for (Map.Entry<Integer, String> entry : categoryNames.entrySet()) {
                if (entry.getValue().equals(categoryStr)) {
                    categoryId = entry.getKey();
                    break;
                }
            }

            double price = Double.parseDouble(priceStr);
            int quantity = Integer.parseInt(quantityStr);

            if (price <= 0 || quantity < 0) {
                resultArea.setText("ap ap ufasod ar sheidzleba da arc is gabedo ro raodenoba ar gqondes");
                return;
            }

            Integer barcodeInt = categoryId*quantity;
            // Create and save the product
            Product product = new Product(name, categoryId, price, quantity,barcodeInt.toString());
            boolean success = DatabaseManager.createProduct(product);

            if (success) {
                resultArea.setText("Product deemata:\n\n" +
                                  "Name: " + name + "\n" +
                                  "Category: " + categoryStr + "\n" +
                                  "Price: $" + String.format("%.2f", price) + "\n" +
                                  "Quantity: " + quantity);

                //cleeeaaarrr
                addProductNameField.clear();
                addProductCategoryComboBox.setValue(null);
                addProductPriceField.clear();
                addProductQuantityField.clear();
            } else {
                resultArea.setText("Failed to add product. Please try again.");
            }
        } catch (NumberFormatException e) {
            resultArea.setText("Invalid price or quantity. Please enter numeric values.");
        }
    }

    // search for a product to update
    private void searchProductToUpdate() {
        String productName = updateProductSearchField.getText().trim();

        if (productName.isEmpty()) {
            resultArea.setText("Please enter a product name to search");
            return;
        }

        Product product = DatabaseManager.getProductByName(productName);

        if (product == null) {
            resultArea.setText("ver vipove produqti ma saxelit: " + productName);
            updateProductButton.setDisable(true);
            return;
        }

        // Store the product for later use
        currentProductForUpdate = product;

        // Populate the fields with the product data
        updateProductNameField.setText(product.getName());

        // Set the category in the combo box
        String categoryName = categoryNames.getOrDefault(product.getCategoryId(), "Unknown Category");
        updateProductCategoryComboBox.setValue(categoryName);

        updateProductPriceField.setText(String.format("%.2f", product.getPrice()));
        updateProductQuantityField.setText(String.valueOf(product.getQuantityInStock()));

        updateProductButton.setDisable(false);

        resultArea.setText("Product found. racxa ginda is sheucvale .");
    }

    // Method to update a product
    private void updateProduct() {
        if (currentProductForUpdate == null) {
            resultArea.setText("No product selected for update");
            return;
        }

        String name = updateProductNameField.getText().trim();
        String categoryStr = updateProductCategoryComboBox.getValue();
        String priceStr = updateProductPriceField.getText().trim();
        String quantityStr = updateProductQuantityField.getText().trim();

        // Validate inputs
        if (name.isEmpty() || categoryStr == null || priceStr.isEmpty() || quantityStr.isEmpty()) {
            resultArea.setText("yvela field shemivseee");
            return;
        }

        try {
            // Convert category name to ID
            int categoryId = 1; // Default to Groceries
            for (Map.Entry<Integer, String> entry : categoryNames.entrySet()) {
                if (entry.getValue().equals(categoryStr)) {
                    categoryId = entry.getKey();
                    break;
                }
            }

            double price = Double.parseDouble(priceStr);
            int quantity = Integer.parseInt(quantityStr);

            if (price <= 0 || quantity < 0) {
                resultArea.setText("ap ap ufasod ar sheidzleba da arc is gabedo ro raodenoba ar gqondes");
                return;
            }

            // Create and update the product
            Product product = new Product(currentProductForUpdate.getProductId(), name, categoryId, price, quantity);
            boolean success = DatabaseManager.updateProduct(product);

            if (success) {
                resultArea.setText("Product updated successfully:\n\n" + 
                                  "Name: " + name + "\n" +
                                  "Category: " + categoryStr + "\n" +
                                  "Price:" + String.format("%.2f", price) + "\n" +
                                  "Quantity: " + quantity);

                updateProductNameField.clear();
                updateProductCategoryComboBox.setValue(null);
                updateProductPriceField.clear();
                updateProductQuantityField.clear();
                updateProductButton.setDisable(true);
                currentProductForUpdate = null;
            } else {
                resultArea.setText("Failed . Please try again.");
            }
        } catch (NumberFormatException e) {
            resultArea.setText("ricxvebi sheiyvaneee");
        }
    }

    // search for products to delete
    private void searchProductToDelete() {
        String searchTerm = deleteProductSearchField.getText().trim();

        if (searchTerm.isEmpty()) {
            resultArea.setText("Enter product name to delete");
            return;
        }

        List<Product> products = DatabaseManager.searchProductsByName(searchTerm);

        if (products.isEmpty()) {
            resultArea.setText("No products found matching: " + searchTerm);
            deleteProductListView.getItems().clear();
            deleteProductButton.setDisable(true);
            return;
        }

        // Clear the list view and add the products
        deleteProductListView.getItems().clear();

        for (Product product : products) {
            boolean isOutOfDate = DatabaseManager.isProductOutOfDate(product.getProductId());
            String categoryName = categoryNames.getOrDefault(product.getCategoryId(), "Unknown Category");

            String listItem = String.format("ID: %d | Name: %s | Category: %s | Price: $%.2f | Quantity: %d | Out of Date: %s",
                product.getProductId(),
                product.getName(),
                categoryName,
                product.getPrice(),
                product.getQuantityInStock(),
                isOutOfDate ? "Yes" : "No"
            );

            deleteProductListView.getItems().add(listItem);
        }

        // Enable the delete button if there are products
        deleteProductButton.setDisable(false);

        resultArea.setText("Found " + products.size() + " products matching: " + searchTerm + 
                          "\nNote: Only out-of-date products can be deleted.");
    }

    // Method to delete a product
    private void deleteProduct() {
        String selectedItem = deleteProductListView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            resultArea.setText("Please select a product to delete");
            return;
        }

        // Extract the product ID from the selected item
        int productId;
        try {
            productId = Integer.parseInt(selectedItem.split("\\|")[0].replace("ID:", "").trim());
        } catch (Exception e) {
            resultArea.setText("Error parsing product ID");
            return;
        }

        // Try to delete the product
        boolean success = DatabaseManager.deleteProduct(productId);

        if (success) {
            resultArea.setText("Product deleted successfully");

            // Remove the item from the list view
            deleteProductListView.getItems().remove(selectedItem);

            // Disable the delete button if the list is empty
            if (deleteProductListView.getItems().isEmpty()) {
                deleteProductButton.setDisable(true);
            }
        } else {
            resultArea.setText("Failed to delete product. It may not be out of date.");
        }
    }

    // Method to add a new employee
    private void addEmployee() {
        String firstName = addEmployeeFirstNameField.getText().trim();
        String lastName = addEmployeeLastNameField.getText().trim();
        String roleStr = addEmployeeRoleComboBox.getValue();
        Integer supermarketId = addEmployeeSupermarketComboBox.getValue();
        String email = addEmployeeEmailField.getText().trim();
        String password = addEmployeePasswordField.getText().trim();
        java.time.LocalDate hireDateLocal = addEmployeeHireDatePicker.getValue();

        // Validate inputs
        if (firstName.isEmpty() || lastName.isEmpty() || roleStr == null || supermarketId == null) {
            resultArea.setText("Please fill in all required fields (First Name, Last Name, Role, Supermarket)");
            return;
        }

        // Convert role name to ID
        int roleId;
        if (roleStr.equals("Admin")) {
            roleId = 1;
        } else if (roleStr.equals("Cashier")) {
            roleId = 2;
        } else {
            resultArea.setText("Invalid role selected");
            return;
        }

        // Check if employee already exists
        if (DatabaseManager.employeeExists(firstName, lastName, supermarketId)) {
            resultArea.setText("An employee with this name already exists in this supermarket");
            return;
        }

        // Create the employee
        Employee employee = new Employee(firstName, lastName, roleId);
        employee.setSupermarketId(supermarketId);

        // Set email if provided
        if (!email.isEmpty()) {
            employee.setEmail(email);
        }

        // Set password if provided
        if (!password.isEmpty()) {
            employee.setPasswordHash(password);
        }

        // Set hire date if provided
        if (hireDateLocal != null) {
            // Convert LocalDate to java.util.Date
            java.util.Date hireDate = java.util.Date.from(
                hireDateLocal.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()
            );
            employee.setHireDate(hireDate);
        }

        // Save the employee
        boolean success = DatabaseManager.createEmployee(employee);

        if (success) {
            StringBuilder resultMessage = new StringBuilder();
            resultMessage.append("Employee added successfully:\n")
                .append("------------------------------------------\n")
                .append("Employee ID: ").append(employee.getEmployeeId()).append("\n")
                .append("Name: ").append(firstName).append(" ").append(lastName).append("\n")
                .append("Role: ").append(roleStr).append("\n")
                .append("Supermarket ID: ").append(supermarketId).append("\n");

            if (!email.isEmpty()) {
                resultMessage.append("Email: ").append(email).append("\n");
            }

            if (hireDateLocal != null) {
                resultMessage.append("Hire Date: ").append(hireDateLocal).append("\n");
            }

            resultArea.setText(resultMessage.toString());

            // Clear the fields
            addEmployeeFirstNameField.clear();
            addEmployeeLastNameField.clear();
            addEmployeeRoleComboBox.setValue(null);
            addEmployeeSupermarketComboBox.setValue(null);
            addEmployeeEmailField.clear();
            addEmployeePasswordField.clear();
            addEmployeeHireDatePicker.setValue(java.time.LocalDate.now());
        } else {
            resultArea.setText("Failed to add employee. Please try again.");
        }
    }

    // Method to search for an employee to update
    private void searchEmployeeToUpdate() {
        String employeeName = updateEmployeeSearchField.getText().trim();
        int supermarketId = updateEmployeeSupermarketSearchComboBox.getValue();

        if (employeeName.isEmpty()) {
            resultArea.setText("Please enter an employee name to search");
            return;
        }

        List<Employee> employees = DatabaseManager.searchEmployeesByName(employeeName, supermarketId);

        if (employees.isEmpty()) {
            resultArea.setText("No employees found matching: " + employeeName + " in supermarket ID: " + supermarketId);
            updateEmployeeButton.setDisable(true);
            return;
        }

        // For simplicity, use the first matching employee
        Employee employee = employees.get(0);

        // Store the employee for later use
        currentEmployeeForUpdate = employee;

        // Populate the fields with the employee data
        updateEmployeeFirstNameField.setText(employee.getFirstName());
        updateEmployeeLastNameField.setText(employee.getLastName());

        // Set the role in the combo box
        String roleName;
        switch (employee.getRoleId()) {
            case 1:
                roleName = "Admin";
                break;
            case 2:
                roleName = "Cashier";
                break;
            default:
                roleName = "Unknown Role";
        }
        updateEmployeeRoleComboBox.setValue(roleName);

        // Set the supermarket in the combo box
        updateEmployeeSupermarketComboBox.setValue(employee.getSupermarketId());

        // Set email if available
        if (employee.getEmail() != null) {
            updateEmployeeEmailField.setText(employee.getEmail());
        } else {
            updateEmployeeEmailField.clear();
        }

        // Set password if available (usually not displayed for security reasons)
        updateEmployeePasswordField.clear();

        // Set hire date if available
        if (employee.getHireDate() != null) {
            // Convert java.util.Date to java.time.LocalDate
            java.time.LocalDate hireDate = employee.getHireDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
            updateEmployeeHireDatePicker.setValue(hireDate);
        } else {
            updateEmployeeHireDatePicker.setValue(null);
        }

        updateEmployeeButton.setDisable(false);

        resultArea.setText("Employee found. You can now update the details.");
    }

    // Method to update an employee
    private void updateEmployee() {
        if (currentEmployeeForUpdate == null) {
            resultArea.setText("No employee selected for update");
            return;
        }

        String firstName = updateEmployeeFirstNameField.getText().trim();
        String lastName = updateEmployeeLastNameField.getText().trim();
        String roleStr = updateEmployeeRoleComboBox.getValue();
        Integer supermarketId = updateEmployeeSupermarketComboBox.getValue();
        String email = updateEmployeeEmailField.getText().trim();
        String password = updateEmployeePasswordField.getText().trim();
        java.time.LocalDate hireDateLocal = updateEmployeeHireDatePicker.getValue();

        // Validate inputs
        if (firstName.isEmpty() || lastName.isEmpty() || roleStr == null || supermarketId == null) {
            resultArea.setText("Please fill in all required fields (First Name, Last Name, Role, Supermarket)");
            return;
        }

        // Convert role name to ID
        int roleId;
        if (roleStr.equals("Admin")) {
            roleId = 1;
        } else if (roleStr.equals("Cashier")) {
            roleId = 2;
        } else {
            resultArea.setText("Invalid role selected");
            return;
        }

        // Update the employee object
        currentEmployeeForUpdate.setFirstName(firstName);
        currentEmployeeForUpdate.setLastName(lastName);
        currentEmployeeForUpdate.setRoleId(roleId);
        currentEmployeeForUpdate.setSupermarketId(supermarketId);

        // Update email if provided
        if (!email.isEmpty()) {
            currentEmployeeForUpdate.setEmail(email);
        }

        // Update password if provided
        if (!password.isEmpty()) {
            currentEmployeeForUpdate.setPasswordHash(password);
        }

        // Update hire date if provided
        if (hireDateLocal != null) {
            // Convert LocalDate to java.util.Date
            java.util.Date hireDate = java.util.Date.from(
                hireDateLocal.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()
            );
            currentEmployeeForUpdate.setHireDate(hireDate);
        }

        // Save the updated employee
        boolean success = DatabaseManager.updateEmployee(currentEmployeeForUpdate);

        if (success) {
            StringBuilder resultMessage = new StringBuilder();
            resultMessage.append("Employee updated successfully:\n")
                .append("------------------------------------------\n")
                .append("Employee ID: ").append(currentEmployeeForUpdate.getEmployeeId()).append("\n")
                .append("Name: ").append(firstName).append(" ").append(lastName).append("\n")
                .append("Role: ").append(roleStr).append("\n")
                .append("Supermarket ID: ").append(supermarketId).append("\n");

            if (!email.isEmpty()) {
                resultMessage.append("Email: ").append(email).append("\n");
            }

            if (hireDateLocal != null) {
                resultMessage.append("Hire Date: ").append(hireDateLocal).append("\n");
            }

            resultArea.setText(resultMessage.toString());

            // Clear the fields and reset the form
            updateEmployeeSearchField.clear();
            updateEmployeeFirstNameField.clear();
            updateEmployeeLastNameField.clear();
            updateEmployeeRoleComboBox.setValue(null);
            updateEmployeeSupermarketComboBox.setValue(null);
            updateEmployeeEmailField.clear();
            updateEmployeePasswordField.clear();
            updateEmployeeHireDatePicker.setValue(null);
            updateEmployeeButton.setDisable(true);
            currentEmployeeForUpdate = null;
        } else {
            resultArea.setText("Failed to update employee. Please try again.");
        }
    }

    // Method to search for employees to delete
    private void searchEmployeeToDelete() {
        String searchTerm = deleteEmployeeSearchField.getText().trim();
        int supermarketId = deleteEmployeeSupermarketComboBox.getValue();

        if (searchTerm.isEmpty()) {
            resultArea.setText("Please enter an employee name to search");
            return;
        }

        List<Employee> employees = DatabaseManager.searchEmployeesByName(searchTerm, supermarketId);

        if (employees.isEmpty()) {
            resultArea.setText("No employees found matching: " + searchTerm + " in supermarket ID: " + supermarketId);
            deleteEmployeeListView.getItems().clear();
            deleteEmployeeButton.setDisable(true);
            employeesToDelete.clear();
            return;
        }

        // Clear the list view and add the employees
        deleteEmployeeListView.getItems().clear();
        employeesToDelete.clear();

        for (Employee employee : employees) {
            String roleName;
            switch (employee.getRoleId()) {
                case 1:
                    roleName = "Admin";
                    break;
                case 2:
                    roleName = "Cashier";
                    break;
                default:
                    roleName = "Unknown Role";
            }

            String listItem = String.format("ID: %d | Name: %s %s | Role: %s | Supermarket ID: %d",
                employee.getEmployeeId(),
                employee.getFirstName(),
                employee.getLastName(),
                roleName,
                employee.getSupermarketId()
            );

            deleteEmployeeListView.getItems().add(listItem);
            employeesToDelete.add(employee);
        }

        // Enable the delete button if there are employees
        deleteEmployeeButton.setDisable(false);

        resultArea.setText("Found " + employees.size() + " employees matching: " + searchTerm + " in supermarket ID: " + supermarketId);
    }

    // Method to delete an employee
    private void deleteEmployee() {
        int selectedIndex = deleteEmployeeListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex < 0 || selectedIndex >= employeesToDelete.size()) {
            resultArea.setText("Please select an employee to delete");
            return;
        }

        Employee employeeToDelete = employeesToDelete.get(selectedIndex);

        // Try to delete the employee
        boolean success = DatabaseManager.deleteEmployee(employeeToDelete.getEmployeeId());

        if (success) {
            resultArea.setText("Employee deleted successfully");

            // Remove the item from the list view and the employees list
            deleteEmployeeListView.getItems().remove(selectedIndex);
            employeesToDelete.remove(selectedIndex);

            // Disable the delete button if the list is empty
            if (deleteEmployeeListView.getItems().isEmpty()) {
                deleteEmployeeButton.setDisable(true);
            }
        } else {
            resultArea.setText("Failed to delete employee. Please try again.");
        }
    }

    private void handleLogout() {
        Alert confirmLogout = new Alert(Alert.AlertType.CONFIRMATION);
        confirmLogout.setTitle("Logout Confirmation");
        confirmLogout.setHeaderText(null);
        confirmLogout.setContentText("Are you sure you want to logout?");

        confirmLogout.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                primaryStage.close();

                try {
                    Login loginApp = new Login();
                    Stage loginStage = new Stage();
                    loginApp.start(loginStage);
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Logout Error", "Failed to open login screen.");
                }
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to load categories from database
    private void loadCategoriesFromDatabase() {
        List<ProductCategory> categories = DatabaseManager.getAllCategories();
        categoryNames.clear();

        for (ProductCategory category : categories) {
            categoryNames.put(category.getCategoryID(), category.getCategoryName());
        }
    }

    // Method to update all category combo boxes
    private void updateCategoryComboBoxes() {
        // Clear existing items
        addProductCategoryComboBox.getItems().clear();

        // Add categories from the map
        for (String categoryName : categoryNames.values()) {
            addProductCategoryComboBox.getItems().add(categoryName);
        }

        // Update the update product combo box if it's initialized
        if (updateProductCategoryComboBox != null) {
            updateProductCategoryComboBox.getItems().clear();
            for (String categoryName : categoryNames.values()) {
                updateProductCategoryComboBox.getItems().add(categoryName);
            }
        }
    }

    // Method to update the categories list view
    private void updateCategoriesListView(ListView<String> listView) {
        listView.getItems().clear();
        for (Map.Entry<Integer, String> entry : categoryNames.entrySet()) {
            listView.getItems().add(entry.getKey() + ": " + entry.getValue());
        }
    }

    // Method to add a new category
    private void addCategory(TextField categoryNameField, ListView<String> categoriesListView) {
        String categoryName = categoryNameField.getText().trim();

        if (categoryName.isEmpty()) {
            resultArea.setText("Please enter a category name");
            return;
        }

        // Check if category already exists
        for (String existingCategory : categoryNames.values()) {
            if (existingCategory.equalsIgnoreCase(categoryName)) {
                resultArea.setText("Category already exists: " + categoryName);
                return;
            }
        }

        // Add category to database
        boolean success = DatabaseManager.createCategory(categoryName);

        if (success) {
            resultArea.setText("Category added successfully: " + categoryName);

            // Reload categories from database
            loadCategoriesFromDatabase();

            // Update UI components
            updateCategoryComboBoxes();
            updateCategoriesListView(categoriesListView);

            // Clear the input field
            categoryNameField.clear();
        } else {
            resultArea.setText("Failed to add category. Please try again.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
