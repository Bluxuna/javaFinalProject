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

    private TextField productIdField;
    private TextField quantityField;
    private TextField couponPercentageField;

    private VBox cartItemsVBox;
    private ScrollPane cartScrollPane;

    private VBox itemPricesVBox;
    private Label totalAmountLabel;
    private Button calculateTotalButton;
    private Button makeTransactionButton;

    private Map<Integer, Double> productDatabase;
    private double currentTotal = 0.0;

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        productDatabase = new HashMap<>();
        productDatabase.put(101, 15.99);
        productDatabase.put(102, 2.50);
        productDatabase.put(103, 50.00);
        productDatabase.put(104, 8.75);
        productDatabase.put(105, 1.00);

        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #2c3e50;");

        Label appTitle = new Label("Cashier Window");
        appTitle.setFont(new Font("Arial", 30));
        appTitle.setStyle("-fx-text-fill: white;");
        HBox.setHgrow(appTitle, Priority.ALWAYS);

        Button logoutButton = new Button("Logout");
        logoutButton.setFont(new Font("Arial", 14));
        logoutButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutButton.setOnAction(e -> handleLogout());

        topBar.getChildren().addAll(appTitle, logoutButton);

        VBox leftSection = new VBox(15);
        leftSection.setPadding(new Insets(20));
        leftSection.setStyle("-fx-background-color: #ecf0f1;");

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
        });

        inputGrid.addRow(1, new Label("Quantity:"), quantityField = new TextField("1"));
        quantityField.setPromptText("e.g., 1");
        quantityField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                quantityField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });

        inputGrid.addRow(2, new Label("Coupon %:"), couponPercentageField = new TextField("0"));
        couponPercentageField.setPromptText("e.g., 10 (for 10%)");
        couponPercentageField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                couponPercentageField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });

        Button addItemButton = new Button("Add Item");
        addItemButton.setPrefWidth(Double.MAX_VALUE);
        addItemButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");

        Label cartTitle = new Label("Current Order:");
        cartTitle.setFont(new Font("Arial", 18));
        cartItemsVBox = new VBox(7);
        cartItemsVBox.setPadding(new Insets(5));
        cartItemsVBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1px; -fx-background-color: white;");

        cartScrollPane = new ScrollPane(cartItemsVBox);
        cartScrollPane.setFitToWidth(true);
        cartScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        cartScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        leftSection.getChildren().addAll(inputTitle, inputGrid, addItemButton, new Separator(), cartTitle, cartScrollPane);
        VBox.setVgrow(cartScrollPane, Priority.ALWAYS);

        VBox rightSection = new VBox(15);
        rightSection.setPadding(new Insets(20));
        rightSection.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-width: 0 0 0 1px;");

        Label pricesTitle = new Label("Itemized Prices:");
        pricesTitle.setFont(new Font("Arial", 18));
        itemPricesVBox = new VBox(5);
        itemPricesVBox.setPadding(new Insets(5));
        itemPricesVBox.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1px; -fx-background-color: white;");
        ScrollPane pricesScrollPane = new ScrollPane(itemPricesVBox);
        pricesScrollPane.setFitToWidth(true);
        pricesScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        pricesScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

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

        makeTransactionButton = new Button("Make Transaction");
        makeTransactionButton.setPrefWidth(Double.MAX_VALUE);
        makeTransactionButton.setPrefHeight(60);
        makeTransactionButton.setFont(new Font("Arial", 20));
        makeTransactionButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold;");

        rightSection.getChildren().addAll(pricesTitle, pricesScrollPane, new Separator(), totalHBox, calculateTotalButton, makeTransactionButton);
        VBox.setVgrow(pricesScrollPane, Priority.ALWAYS);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topBar);

        SplitPane contentSplitPane = new SplitPane();
        contentSplitPane.getItems().addAll(leftSection, rightSection);
        contentSplitPane.setDividerPositions(0.6);
        mainLayout.setCenter(contentSplitPane);

        addItemButton.setOnAction(e -> addItem());
        calculateTotalButton.setOnAction(e -> updateTotalDisplay());
        makeTransactionButton.setOnAction(e -> makeTransaction());

        Scene scene = new Scene(mainLayout, 1000, 700);
        stage.setTitle("Cashier app");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

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

            HBox itemRow = new HBox(10);
            itemRow.setAlignment(Pos.CENTER_LEFT);
            itemRow.setPadding(new Insets(2, 5, 2, 5));
            itemRow.setStyle("-fx-background-color: #f8f8f8; -fx-border-color: #eee; -fx-border-width: 0 0 1px 0;");

            Label itemInfo = new Label(String.format("ID: %d | Qty: %d | Price: $%.2f (%.0f%% off)", productId, quantity, finalItemPrice, (double)couponPercentage));
            itemInfo.setFont(new Font("Arial", 14));
            HBox.setHgrow(itemInfo, Priority.ALWAYS);

            Button deleteButton = new Button("X");
            deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 10px;");
            deleteButton.setPrefSize(25, 25);
            deleteButton.setOnAction(e -> {
                cartItemsVBox.getChildren().remove(itemRow);
                updateItemizedPrices();
                updateTotalDisplay();
            });

            itemRow.getChildren().addAll(itemInfo, deleteButton);
            cartItemsVBox.getChildren().add(itemRow);

            Label priceLabel = new Label(String.format("Product %d x %d: $%.2f", productId, quantity, finalItemPrice));
            priceLabel.setFont(new Font("Arial", 14));
            itemPricesVBox.getChildren().add(priceLabel);

            productIdField.clear();
            quantityField.setText("1");
            couponPercentageField.setText("0");

            updateTotalDisplay();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numbers for Product ID, Quantity, and Coupon %.");
        }
    }

    private void updateItemizedPrices() {
        itemPricesVBox.getChildren().clear();
        for (int i = 0; i < cartItemsVBox.getChildren().size(); i++) {
            HBox itemRow = (HBox) cartItemsVBox.getChildren().get(i);
            Label itemInfoLabel = (Label) itemRow.getChildren().get(0);
            String itemText = itemInfoLabel.getText();

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

    private void updateTotalDisplay() {
        currentTotal = 0.0;
        for (int i = 0; i < cartItemsVBox.getChildren().size(); i++) {
            HBox itemRow = (HBox) cartItemsVBox.getChildren().get(i);
            Label itemInfoLabel = (Label) itemRow.getChildren().get(0);
            String itemText = itemInfoLabel.getText();
            try {
                String pricePart = itemText.substring(itemText.indexOf("Price: $") + 8, itemText.indexOf(" ("));
                currentTotal += Double.parseDouble(pricePart);
            } catch (Exception e) {
                System.err.println("Error parsing price from item for total calculation: " + itemText + " - " + e.getMessage());
            }
        }
        totalAmountLabel.setText(String.format("$%.2f", currentTotal));
    }

    private void makeTransaction() {
        if (currentTotal <= 0 && cartItemsVBox.getChildren().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Transaction Error", "No items in cart to process transaction.");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Transaction Complete", String.format("Transaction of $%.2f processed successfully!", currentTotal));

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

    public static void main(String[] args) {
        launch(args);
    }
}