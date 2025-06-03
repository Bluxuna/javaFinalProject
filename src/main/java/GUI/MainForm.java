package GUI;

import Databases.DatabaseManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Databases.dbObjects.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;


public class MainForm extends Application {

    private TextField productIdField;
    private TextField quantityField;
    private TextField couponPercentageField;

    private VBox cartItemsVBox;
    private ScrollPane cartScrollPane;

    private VBox itemPricesVBox;
    private Label totalAmountLabel;
    private Button makeTransactionButton;

    private Map<Integer, Double> productDatabase;
    private double currentTotal = 0.0;
    private Stack<Product> productStack = new Stack<>();

    private Stage primaryStage;
    private String fname,lname;
    private int marketID;
    public MainForm(String fname,String lname,int marketID) {
        this.fname=fname;
        this.lname=lname;
        this.marketID=marketID;
    }

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

        Label appTitle = new Label("Cashier: " +  this.fname + " " + this.lname + " marketID: " + this.marketID);
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


        makeTransactionButton = new Button("Make Transaction");
        makeTransactionButton.setPrefWidth(Double.MAX_VALUE);
        makeTransactionButton.setPrefHeight(60);
        makeTransactionButton.setFont(new Font("Arial", 20));
        makeTransactionButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold;");

        rightSection.getChildren().addAll(pricesTitle, pricesScrollPane, new Separator(), totalHBox, makeTransactionButton);
        VBox.setVgrow(pricesScrollPane, Priority.ALWAYS);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topBar);

        SplitPane contentSplitPane = new SplitPane();
        contentSplitPane.getItems().addAll(leftSection, rightSection);
        contentSplitPane.setDividerPositions(0.6);
        mainLayout.setCenter(contentSplitPane);

        addItemButton.setOnAction(e -> addItem());
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
                showAlert(Alert.AlertType.WARNING, "Input Error", 
                    "Please enter valid positive numbers for Product ID, Quantity, and a Coupon % between 0-100.");
                return;
            }

            // Get product from database
            Product product = DatabaseManager.getProductById(productId,quantity);

            if (product == null) {
                showAlert(Alert.AlertType.ERROR, "Product Not Found", 
                    "Product ID " + productId + " does not exist or has insufficient inventory.");
                return;
            }

            // Push product to stack
            productStack.push(product);

            double basePrice = product.getPrice();
            double itemPrice = basePrice * quantity;
            double discountAmount = itemPrice * (couponPercentage / 100.0);
            double finalItemPrice = itemPrice - discountAmount;

            HBox itemRow = new HBox(10);
            itemRow.setAlignment(Pos.CENTER_LEFT);
            itemRow.setPadding(new Insets(2, 5, 2, 5));
            itemRow.setStyle("-fx-background-color: #f8f8f8; -fx-border-color: #eee; -fx-border-width: 0 0 1px 0;");

            Label itemInfo = new Label(String.format("ID: %d | Name: %s | Qty: %d | Price: $%.2f (%.0f%% off)", 
                productId, product.getName(), quantity, finalItemPrice, (double)couponPercentage));
            itemInfo.setFont(new Font("Arial", 14));
            HBox.setHgrow(itemInfo, Priority.ALWAYS);

            Button deleteButton = new Button("X");
            deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 10px;");
            deleteButton.setPrefSize(25, 25);
            deleteButton.setOnAction(e -> {
                cartItemsVBox.getChildren().remove(itemRow);
                updateItemizedPrices();
                updateTotalDisplay();

                // Pop product from stack if not empty
                if (!productStack.isEmpty()) {
                    productStack.pop();
                }
            });

            itemRow.getChildren().addAll(itemInfo, deleteButton);
            cartItemsVBox.getChildren().add(itemRow);

            Label priceLabel = new Label(String.format("%s x %d: $%.2f", 
                product.getName(), quantity, finalItemPrice));
            priceLabel.setFont(new Font("Arial", 14));
            itemPricesVBox.getChildren().add(priceLabel);

            productIdField.clear();
            quantityField.setText("1");
            couponPercentageField.setText("0");

            updateTotalDisplay();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", 
                "Please enter valid numbers for Product ID, Quantity, and Coupon %.");
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
        for (Product p : productStack) {
            System.out.println(p.getName());
        }

        showPaymentSelectionDialog();
    }

    private void showPaymentSelectionDialog() {
        Stage paymentStage = new Stage();
        paymentStage.initModality(Modality.APPLICATION_MODAL);
        paymentStage.initOwner(primaryStage);
        paymentStage.setTitle("Payment Selection");
        paymentStage.setResizable(false);

        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle("-fx-background-color: #ecf0f1;");

        Label titleLabel = new Label("Select Payment Method");
        titleLabel.setFont(new Font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        Label totalLabel = new Label(String.format("Total Amount: $%.2f", currentTotal));
        totalLabel.setFont(new Font("Arial", 18));
        totalLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        Button cardButton = new Button("Card Payment");
        cardButton.setPrefSize(150, 60);
        cardButton.setFont(new Font("Arial", 16));
        cardButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        cardButton.setOnAction(e -> {
            paymentStage.close();
            processCardPayment();
        });

        Button cashButton = new Button("Cash Payment");
        cashButton.setPrefSize(150, 60);
        cashButton.setFont(new Font("Arial", 16));
        cashButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        cashButton.setOnAction(e -> {
            paymentStage.close();
            showCashPaymentDialog();
        });
        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefSize(100, 40);
        cancelButton.setFont(new Font("Arial", 14));
        cancelButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        cancelButton.setOnAction(e -> paymentStage.close());

        buttonBox.getChildren().addAll(cardButton, cashButton);
        mainLayout.getChildren().addAll(titleLabel, totalLabel, buttonBox, cancelButton);

        Scene scene = new Scene(mainLayout, 400, 300);
        paymentStage.setScene(scene);
        paymentStage.showAndWait();
    }

    private void processCardPayment() {
        showCardPaymentDialog();
    }

    private void showCardPaymentDialog() {
        Stage cardStage = new Stage();
        cardStage.initModality(Modality.APPLICATION_MODAL);
        cardStage.initOwner(primaryStage);
        cardStage.setTitle("Card Payment");
        cardStage.setResizable(false);

        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle("-fx-background-color: #ecf0f1;");

        Label titleLabel = new Label("Card Payment");
        titleLabel.setFont(new Font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        Label totalLabel = new Label(String.format("Total Amount: $%.2f", currentTotal));
        totalLabel.setFont(new Font("Arial", 18));
        totalLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");

        // Card Number Input
        HBox cardNumberBox = new HBox(10);
        cardNumberBox.setAlignment(Pos.CENTER);
        Label cardNumberLabel = new Label("Card Number:");
        cardNumberLabel.setFont(new Font("Arial", 16));

        TextField cardNumberField = new TextField();
        cardNumberField.setPromptText("Enter card number");
        cardNumberField.setPrefWidth(200);
        cardNumberField.setFont(new Font("Arial", 16));

        cardNumberBox.getChildren().addAll(cardNumberLabel, cardNumberField);

        // Expiry Date Input
        HBox expiryDateBox = new HBox(10);
        expiryDateBox.setAlignment(Pos.CENTER);
        Label expiryDateLabel = new Label("Expiry Date (MM/YY):");
        expiryDateLabel.setFont(new Font("Arial", 16));

        TextField expiryDateField = new TextField();
        expiryDateField.setPromptText("MM/YY");
        expiryDateField.setPrefWidth(100);
        expiryDateField.setFont(new Font("Arial", 16));

        expiryDateBox.getChildren().addAll(expiryDateLabel, expiryDateField);

        // CVV Input
        HBox cvvBox = new HBox(10);
        cvvBox.setAlignment(Pos.CENTER);
        Label cvvLabel = new Label("CVV:");
        cvvLabel.setFont(new Font("Arial", 16));

        TextField cvvField = new TextField();
        cvvField.setPromptText("123");
        cvvField.setPrefWidth(80);
        cvvField.setFont(new Font("Arial", 16));

        cvvBox.getChildren().addAll(cvvLabel, cvvField);

        // Cardholder Name Input
        HBox cardholderBox = new HBox(10);
        cardholderBox.setAlignment(Pos.CENTER);
        Label cardholderLabel = new Label("Cardholder Name:");
        cardholderLabel.setFont(new Font("Arial", 16));

        TextField cardholderField = new TextField();
        cardholderField.setPromptText("Enter name on card");
        cardholderField.setPrefWidth(200);
        cardholderField.setFont(new Font("Arial", 16));

        cardholderBox.getChildren().addAll(cardholderLabel, cardholderField);

        // Buttons
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        Button processButton = new Button("Process Payment");
        processButton.setPrefSize(180, 50);
        processButton.setFont(new Font("Arial", 16));
        processButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");

        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefSize(100, 50);
        cancelButton.setFont(new Font("Arial", 16));
        cancelButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");

        buttonBox.getChildren().addAll(processButton, cancelButton);

        // Add validation for card number (only digits)
        cardNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cardNumberField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.length() > 16) {
                cardNumberField.setText(newValue.substring(0, 16));
            }
        });

        // Add validation for expiry date (MM/YY format)
        expiryDateField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,2}/\\d{0,2}|\\d{0,2}")) {
                expiryDateField.setText(oldValue);
            } else if (newValue.length() == 2 && oldValue.length() == 1 && !newValue.contains("/")) {
                expiryDateField.setText(newValue + "/");
            }
        });

        // Add validation for CVV (only digits, max 3-4 digits)
        cvvField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cvvField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.length() > 4) {
                cvvField.setText(newValue.substring(0, 4));
            }
        });

        // Process payment button action
        processButton.setOnAction(e -> {
            // Validate inputs
            boolean isValid = true;
            String errorMessage = "";

            if (cardNumberField.getText().trim().isEmpty() || cardNumberField.getText().length() < 13) {
                isValid = false;
                errorMessage += "Please enter a valid card number (13-16 digits).\n";
            }

            if (expiryDateField.getText().trim().isEmpty() || !expiryDateField.getText().matches("\\d{2}/\\d{2}")) {
                isValid = false;
                errorMessage += "Please enter a valid expiry date (MM/YY).\n";
            }

            if (cvvField.getText().trim().isEmpty() || cvvField.getText().length() < 3) {
                isValid = false;
                errorMessage += "Please enter a valid CVV (3-4 digits).\n";
            }

            if (cardholderField.getText().trim().isEmpty()) {
                isValid = false;
                errorMessage += "Please enter the cardholder name.\n";
            }

            if (isValid) {
                // Process the card payment
                cardStage.close();

                // Get card details for database (if needed)
                String cardNumber = cardNumberField.getText();
                String expiryDate = expiryDateField.getText();
                String cvv = cvvField.getText();
                String cardholderName = cardholderField.getText();

                // Store last 4 digits for receipt
                String lastFourDigits = cardNumber.substring(cardNumber.length() - 4);

                // Complete the transaction with card details
                completeCardTransaction(lastFourDigits, cardholderName);
            } else {
                // Show error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Validation Error");
                alert.setHeaderText(null);
                alert.setContentText(errorMessage);
                alert.showAndWait();
            }
        });

        // Cancel button action
        cancelButton.setOnAction(e -> cardStage.close());

        mainLayout.getChildren().addAll(titleLabel, totalLabel, cardNumberBox, expiryDateBox, cvvBox, cardholderBox, buttonBox);

        Scene scene = new Scene(mainLayout, 500, 450);
        cardStage.setScene(scene);

        // Show the stage
        cardStage.show();
        cardNumberField.requestFocus();
    }

    private void completeCardTransaction(String lastFourDigits, String cardholderName) {
        // Complete the transaction with card payment method
        // We'll pass the card information to be displayed in the receipt
        String cardInfo = "Card ending in " + lastFourDigits + " (" + cardholderName + ")";
        completeTransaction("Card: " + cardInfo);
    }

    private void showCashPaymentDialog() {
        Stage cashStage = new Stage();
        cashStage.initModality(Modality.APPLICATION_MODAL);
        cashStage.initOwner(primaryStage);
        cashStage.setTitle("Cash Payment");
        cashStage.setResizable(false);

        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle("-fx-background-color: #ecf0f1;");

        Label titleLabel = new Label("Cash Payment");
        titleLabel.setFont(new Font("Arial", 24));
        titleLabel.setStyle("-fx-font-weight: bold;");

        Label totalLabel = new Label(String.format("Total Amount: $%.2f", currentTotal));
        totalLabel.setFont(new Font("Arial", 18));
        totalLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");

        HBox cashInputBox = new HBox(10);
        cashInputBox.setAlignment(Pos.CENTER);
        Label cashLabel = new Label("Cash Given: $");
        cashLabel.setFont(new Font("Arial", 16));

        TextField cashField = new TextField();
        cashField.setPromptText("Enter amount");
        cashField.setPrefWidth(120);
        cashField.setFont(new Font("Arial", 16));

        cashInputBox.getChildren().addAll(cashLabel, cashField);

        Label changeLabel = new Label("Change: $0.00");
        changeLabel.setFont(new Font("Arial", 18));
        changeLabel.setStyle("-fx-text-fill: #666666; -fx-font-weight: bold;");

        // validacia
        cashField.setOnKeyReleased(e -> {
            String text = cashField.getText().trim();
            if (text.isEmpty()) {
                changeLabel.setText("Change: $0.00");
                changeLabel.setStyle("-fx-text-fill: #666666; -fx-font-weight: bold;");
                return;
            }

            try {
                double cashGiven = Double.parseDouble(text);
                double change = cashGiven - currentTotal;
                if (change >= 0) {
                    changeLabel.setText(String.format("Change: $%.2f", change));
                    changeLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                } else {
                    changeLabel.setText(String.format("Need $%.2f more", Math.abs(change)));
                    changeLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                }
            } catch (NumberFormatException ex) {
                changeLabel.setText("Invalid amount");
                changeLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
            }
        });

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button completeButton = new Button("Complete Payment");
        completeButton.setPrefSize(150, 40);
        completeButton.setFont(new Font("Arial", 14));
        completeButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        completeButton.setOnAction(e -> {
            String cashText = cashField.getText().trim();
            if (cashText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a cash amount.");
                return;
            }

            try {
                double cashGiven = Double.parseDouble(cashText);
                if (cashGiven >= currentTotal) {
                    cashStage.close();
                    double change = cashGiven - currentTotal;
                    completeTransaction("Cash", cashGiven, change);
                } else {
                    showAlert(Alert.AlertType.WARNING, "Insufficient Cash",
                            String.format("Cash given ($%.2f) is less than total amount ($%.2f)", cashGiven, currentTotal));
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number for cash amount.");
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefSize(100, 40);
        cancelButton.setFont(new Font("Arial", 14));
        cancelButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        cancelButton.setOnAction(e -> cashStage.close());

        buttonBox.getChildren().addAll(completeButton, cancelButton);
        mainLayout.getChildren().addAll(titleLabel, totalLabel, cashInputBox, changeLabel, buttonBox);

        Scene scene = new Scene(mainLayout, 400, 350);
        cashStage.setScene(scene);

        // Show the stage
        cashStage.show();
        cashField.requestFocus();
    }

    private void completeTransaction(String paymentMethod) {
        completeTransaction(paymentMethod, 0, 0);
    }

    private void completeTransaction(String paymentMethod, double cashGiven, double change) {
        // Create a new Sale record
        Sale sale = new Sale();
        sale.setEmployeeID(1); // Assuming employee ID 1 for now, you might want to get the actual employee ID
        sale.setSupermarketID(this.marketID);
        sale.setTotalAmount(currentTotal);
        sale.setSaleDate(new java.sql.Timestamp(System.currentTimeMillis()));

        // Save the sale to the database
        boolean saleCreated = DatabaseManager.createSale(sale);

        if (saleCreated) {
            // Process each item in the cart
            for (int i = 0; i < cartItemsVBox.getChildren().size(); i++) {
                HBox itemRow = (HBox) cartItemsVBox.getChildren().get(i);
                Label itemInfoLabel = (Label) itemRow.getChildren().get(0);
                String itemText = itemInfoLabel.getText();

                try {
                    // Extract product ID, name, and quantity from the label
                    String idPart = itemText.substring(itemText.indexOf("ID: ") + 4, itemText.indexOf(" | Name:"));
                    int productId = Integer.parseInt(idPart);

                    String namePart = itemText.substring(itemText.indexOf("Name: ") + 6, itemText.indexOf(" | Qty:"));

                    String qtyPart = itemText.substring(itemText.indexOf("Qty: ") + 5, itemText.indexOf(" | Price:"));
                    int quantity = Integer.parseInt(qtyPart);

                    String pricePart = itemText.substring(itemText.indexOf("Price: $") + 8, itemText.indexOf(" ("));
                    double price = Double.parseDouble(pricePart);

                    // Create a SaleItem for this product
                    SaleItem saleItem = new SaleItem();
                    saleItem.setSaleID(sale.getSaleID());
                    saleItem.setProductID(productId);
                    saleItem.setQuantity(quantity);
                    saleItem.setUnitPrice(new java.math.BigDecimal(price / quantity)); // Unit price

                    // Save the sale item to the database
                    DatabaseManager.createSaleItem(saleItem);

                    // Update inventory (reduce quantity)
                    Inventory inventory = new Inventory();
                    inventory.setProductID(productId);

                    // Get current quantity from database and subtract the sold quantity
                    Product dbProduct = DatabaseManager.getProductById(productId, 0);
                    if (dbProduct != null) {
                        int newQuantity = dbProduct.getQuantityInStock() - quantity;
                        inventory.setQuantity(newQuantity > 0 ? newQuantity : 0);
                        DatabaseManager.updateInventory(inventory);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing cart item for database: " + itemText + " - " + e.getMessage());
                }
            }
        }

        StringBuilder message = new StringBuilder();
        message.append("Transaction Made Successfully!\n\n");
        message.append(String.format("Transaction Amount: $%.2f\n", currentTotal));
        message.append(String.format("Payment Method: %s\n", paymentMethod));

        if (paymentMethod.equals("Cash")) {
            message.append(String.format("Cash Given: $%.2f\n", cashGiven));
            message.append(String.format("Change: $%.2f\n", change));
        }
        // For card payments, the payment method already includes the card details
        // so we don't need to add any additional information

        message.append("\nThank you for your purchase!");

        showAlert(Alert.AlertType.INFORMATION, "Transaction Made", message.toString());

        // Clear the cart after successful transaction
        cartItemsVBox.getChildren().clear();
        itemPricesVBox.getChildren().clear();
        currentTotal = 0.0;
        totalAmountLabel.setText("$0.00");

        // Clear the product stack
        productStack.clear();
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
