package GUI;

import Databases.DatabaseManager;
import Databases.dbObjects.Employee;
import Databases.dbObjects.Supermarket;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;

public class EmployeeSearchForm extends Application {
    private Stage primaryStage;
    private VBox root;
    private TextField searchField;
    private ComboBox<Integer> supermarketComboBox;
    private Button searchButton;
    private TextArea resultArea; 

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        stage.setTitle("Employee Search");

        // Make the form full screen
        javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        this.root = new VBox(20);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Employee Search");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Employee search
        Label searchLabel = new Label("Enter employee name to search:");
        searchField = new TextField();
        searchField.setPromptText("Employee name");
        searchField.setPrefWidth(300);

        // Supermarket selection
        Label supermarketLabel = new Label("Select supermarket:");
        supermarketComboBox = new ComboBox<>();
        supermarketComboBox.setPromptText("Select supermarket");
        supermarketComboBox.setPrefWidth(200);

        // Populate supermarket combo box (in a real app, these would come from the database)
        supermarketComboBox.getItems().addAll(1, 2, 3); // Example supermarket IDs
        supermarketComboBox.setValue(1); // Default selection

        searchButton = new Button("Search");
        searchButton.setPrefWidth(100);

        HBox searchBox = new HBox(10);
        searchBox.getChildren().addAll(searchField, supermarketComboBox, searchButton);

        // Result area
        resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefHeight(screenBounds.getHeight() - 200);
        resultArea.setWrapText(true);

        root.getChildren().addAll(
            titleLabel,
            searchLabel,
            searchBox,
            resultArea
        );

        // Set action handlers
        searchButton.setOnAction(e -> searchEmployees());

        // Create a scene
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Employee Search");
        stage.setScene(scene);
        stage.show();
    }

    private void searchEmployees() {
        String searchTerm = searchField.getText().trim();
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

    public static void main(String[] args) {
        launch(args);
    }
}
