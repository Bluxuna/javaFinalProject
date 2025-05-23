package GUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class MainForm extends Application {

    // Input fields
    private TextField productIdField;
    private TextField quantityField;
    private TextField couponPercentageField;

    // Cart display
    private VBox cartItemsVBox; //Hboxebi itemebshi
    private ScrollPane cartScrollPane;

    // Right side display
    private VBox itemPricesVBox; // display each itemebis pricebi
    private Label totalAmountLabel;
    private Button calculateTotalButton;
    private Button makeTransactionButton;

    // Data storage (for demonstration purposes)
    private Map<Integer, Double> productDatabase; // productId -> price
    private double currentTotal = 0.0;

    private Stage primaryStage; // <<--- ADD THIS LINE: Declare a class-level Stage variable

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage; // <<--- ADD THIS LINE: Assign the passed stage to the class variable

        // Initialize dummy product database
        productDatabase = new HashMap<>();
        productDatabase.put(101, 15.99);
        productDatabase.put(102, 2.50);
        productDatabase.put(103, 50.00);
        productDatabase.put(104, 8.75);
        productDatabase.put(105, 1.00);

        // --- 1. Top Bar (Title/Logo & Logout Button) ---
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #2c3e50;");

        Label appTitle = new Label("Cashier Window");
        appTitle.setFont(new Font("Arial", 30));
        appTitle.setStyle("-fx-text-fill: white;");
        HBox.setHgrow(appTitle, Priority.ALWAYS); // Allow title to push other elements

        Button logoutButton = new Button("Logout");
        logoutButton.setFont(new Font("Arial", 14));
        logoutButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutButton.setOnAction(e -> handleLogout()); // Use the new method

        topBar.getChildren().addAll(appTitle, logoutButton);


        // --- 2. Left Section: Item Entry and Cart ---
        VBox leftSection = new VBox(15); // Spacing
        leftSection.setPadding(new Insets(20));
        leftSection.setStyle("-fx-background-color: #ecf0f1;");

        // Input Controls
        Label inputTitle = new Label("Add Item:");
        inputTitle.setFont(new Font("Arial", 18));

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);

        inputGrid.addRow(0, new Label("Product ID:"), productIdField = new TextField());
        productIdField.setPromptText("e.g., 101");
        productIdField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                productIdField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        }); // Only allow digits

        inputGrid.addRow(1, new Label("Quantity:"), quantityField = new TextField("1"));
        quantityField.setPromptText("e.g., 1");
        quantityField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                quantityField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });//mxolod cifrebi

        inputGrid.addRow(2, new Label("Coupon %:"), couponPercentageField = new TextField("0"));
        couponPercentageField.setPromptText("e.g., 10 (for 10%)");
        couponPercentageField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                couponPercentageField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        }); // marto cifrebi

        Button addItemButton = new Button("Add Item");
        addItemButton.setPrefWidth(Double.MAX_VALUE);
        addItemButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");

        // Cart Display Area
        Label cartTitle = new Label("Current Order:");
        cartTitle.setFont(new Font("Arial", 18));
        cartItemsVBox = new VBox(7); // Spacing between items cartebshi
        cartItemsVBox.setPadding(new Insets(5));
        cartItemsVBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1px; -fx-background-color: white;");

        cartScrollPane = new ScrollPane(cartItemsVBox);
        cartScrollPane.setFitToWidth(true); // Ensures content stretches to scroll pane width
        cartScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); //  show vertical scrollbar when needed
        cartScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Never show horizontal scrollbar

        leftSection.getChildren().addAll(inputTitle, inputGrid, addItemButton, new Separator(), cartTitle, cartScrollPane);
        VBox.setVgrow(cartScrollPane, Priority.ALWAYS); // Cart area expands

        // --- 2. R Section: Item Prices, Total, and Transaction Button ---
        VBox rightSection = new VBox(15); // Spacing
        rightSection.setPadding(new Insets(20));
        rightSection.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-width: 0 0 0 1px;"); // Left border

        // Item Prices Display
        Label pricesTitle = new Label("Itemized Prices:");
        pricesTitle.setFont(new Font("Arial", 18));
        itemPricesVBox = new VBox(5); // Spacing between individual prices
        itemPricesVBox.setPadding(new Insets(5));
        itemPricesVBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1px; -fx-background-color: white;");
        ScrollPane pricesScrollPane = new ScrollPane(itemPricesVBox);
        pricesScrollPane.setFitToWidth(true);
        pricesScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        pricesScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Total Display
        HBox totalHBox = new HBox(10);
        totalHBox.setAlignment(Pos.CENTER_LEFT);
        Label totalStaticLabel = new Label("Total:");
        totalStaticLabel.setFont(new Font("Arial", 24));
        totalAmountLabel = new Label("$0.00");
        totalAmountLabel.setFont(new Font("Arial", 28));
        totalAmountLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #27ae60;");
        totalHBox.getChildren().addAll(totalStaticLabel, totalAmountLabel);

        calculateTotalButton = new Button("Calculate Total");
        calculateTotalButton.setPrefWidth(Double.MAX_VALUE);
        calculateTotalButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");

        // Transaction Button
        makeTransactionButton = new Button("Make Transaction");
        makeTransactionButton.setPrefWidth(Double.MAX_VALUE);
        makeTransactionButton.setPrefHeight(60);
        makeTransactionButton.setFont(new Font("Arial", 20));
        makeTransactionButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold;");

        rightSection.getChildren().addAll(pricesTitle, pricesScrollPane, new Separator(), totalHBox, calculateTotalButton, makeTransactionButton);
        VBox.setVgrow(pricesScrollPane, Priority.ALWAYS); // Prices area expands

        // --- 3. Main Layout (BorderPane + SplitPane) ---
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topBar);

        SplitPane contentSplitPane = new SplitPane();
        contentSplitPane.getItems().addAll(leftSection, rightSection);
        contentSplitPane.setDividerPositions(0.6); // 60% for left, 40% for right
        mainLayout.setCenter(contentSplitPane);


        // --- Event Handlers ---
        addItemButton.setOnAction(e -> addItem());
        calculateTotalButton.setOnAction(e -> updateTotalDisplay());
        makeTransactionButton.setOnAction(e -> makeTransaction());

        // --- Scene and Stage ---
        Scene scene = new Scene(mainLayout, 1000, 700);
        stage.setTitle("Cashier app");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    // --- Helper method to add an item to the cart ---
    private void addItem() {
        try {
            int productId = Integer.parseInt(productIdField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            int couponPercentage = Integer.parseInt(couponPercentageField.getText());

            if (productId <= 0 || quantity <= 0 || couponPercentage < 0 || couponPercentage > 100) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter valid positive numbers for Product ID, Quantity, and a Coupon % between 0-100.");
                return;
            }

            if (!productDatabase.containsKey(productId)) {
                showAlert(Alert.AlertType.ERROR, "Product Not Found", "Product ID " + productId + " does not exist in the database.");
                return;
            }

            double basePrice = productDatabase.get(productId);
            double itemPrice = basePrice * quantity;
            double discountAmount = itemPrice * (couponPercentage / 100.0);
            double finalItemPrice = itemPrice - discountAmount;

            //  HBox for the cart item
            HBox itemRow = new HBox(10);
            itemRow.setAlignment(Pos.CENTER_LEFT);
            itemRow.setPadding(new Insets(2, 5, 2, 5));
            itemRow.setStyle("-fx-background-color: #f8f8f8; -fx-border-color: #eee; -fx-border-width: 0 0 1px 0;");

            Label itemInfo = new Label(String.format("ID: %d | Qty: %d | Price: $%.2f (%.0f%% off)", productId, quantity, finalItemPrice, (double)couponPercentage));
            itemInfo.setFont(new Font("Arial", 14));
            HBox.setHgrow(itemInfo, Priority.ALWAYS); // Allow info to take space

            Button deleteButton = new Button("X");
            deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 10px;");
            deleteButton.setPrefSize(25, 25);
            deleteButton.setOnAction(e -> {
                cartItemsVBox.getChildren().remove(itemRow);
                updateItemizedPrices(); // Recalculate prices when item is deleted
                updateTotalDisplay();
            });

            itemRow.getChildren().addAll(itemInfo, deleteButton);
            cartItemsVBox.getChildren().add(itemRow);

            // Add item price to right side display
            Label priceLabel = new Label(String.format("Product %d x %d: $%.2f", productId, quantity, finalItemPrice));
            priceLabel.setFont(new Font("Arial", 14));
            itemPricesVBox.getChildren().add(priceLabel);

            // Clear input
            productIdField.clear();
            quantityField.setText("1");
            couponPercentageField.setText("0");

            // Update total immediately after adding item automatically
            updateTotalDisplay();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numbers for Product ID, Quantity, and Coupon %.");
        }
    }

    // --- Helper method to update the itemized prices display (used when deleting an item) ---
    private void updateItemizedPrices() {
        itemPricesVBox.getChildren().clear();
        for (int i = 0; i < cartItemsVBox.getChildren().size(); i++) {
            HBox itemRow = (HBox) cartItemsVBox.getChildren().get(i);
            Label itemInfoLabel = (Label) itemRow.getChildren().get(0); // Get the Label with item info
            String itemText = itemInfoLabel.getText();

            // Parse product ID, quantity, and price from the itemInfoLabel text
            try {
                String idPart = itemText.substring(itemText.indexOf("ID: ") + 4, itemText.indexOf(" | Qty:"));
                int productId = Integer.parseInt(idPart);

                String qtyPart = itemText.substring(itemText.indexOf("Qty: ") + 5, itemText.indexOf(" | Price:"));
                int quantity = Integer.parseInt(qtyPart);

                String pricePart = itemText.substring(itemText.indexOf("Price: $") + 8, itemText.indexOf(" ("));
                double price = Double.parseDouble(pricePart);

                Label priceLabel = new Label(String.format("Product %d x %d: $%.2f", productId, quantity, price));
                priceLabel.setFont(new Font("Arial", 14));
                itemPricesVBox.getChildren().add(priceLabel);

            } catch (Exception e) {
                System.err.println("Error parsing item info for re-display: " + itemText + " - " + e.getMessage());
            }
        }
    }


    // --- Helper method to calculate and display the total ---
    private void updateTotalDisplay() {
        currentTotal = 0.0;
        for (int i = 0; i < cartItemsVBox.getChildren().size(); i++) {
            HBox itemRow = (HBox) cartItemsVBox.getChildren().get(i);
            Label itemInfoLabel = (Label) itemRow.getChildren().get(0); // Get the Label with item info
            String itemText = itemInfoLabel.getText();
            try {
                // Extract price from the label text
                String pricePart = itemText.substring(itemText.indexOf("Price: $") + 8, itemText.indexOf(" ("));
                currentTotal += Double.parseDouble(pricePart);
            } catch (Exception e) {
                System.err.println("Error parsing price from item for total calculation: " + itemText + " - " + e.getMessage());
            }
        }
        totalAmountLabel.setText(String.format("$%.2f", currentTotal));
    }

    // --- Helper method for transaction ---
    private void makeTransaction() {
        if (currentTotal <= 0 && cartItemsVBox.getChildren().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Transaction Error", "No items in cart to process transaction.");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Transaction Complete", String.format("Transaction of $%.2f processed successfully!", currentTotal));

        // Clear the cart and reset totals after transaction
        cartItemsVBox.getChildren().clear();
        itemPricesVBox.getChildren().clear();
        currentTotal = 0.0;
        totalAmountLabel.setText("$0.00");
    }

    private void handleLogout() {
        Alert confirmLogout = new Alert(Alert.AlertType.CONFIRMATION);
        confirmLogout.setTitle("Logout Confirmation");
        confirmLogout.setHeaderText(null);
        confirmLogout.setContentText("Are you sure you want to logout?");

        confirmLogout.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Close the current MainForm stage
                primaryStage.close(); // <<--- USE THE CLASS-LEVEL VARIABLE HERE

                // Open the Login stage
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
    // --- Helper Method for Alerts ---
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}