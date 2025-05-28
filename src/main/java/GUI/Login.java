package GUI;

import Databases.DatabaseManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
public class Login extends Application {
    private DatabaseManager databaseManager;

    private TextField firstNameField;
    private TextField lastNameField;
    private Button loginButton;
    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        databaseManager = new DatabaseManager();
        this.primaryStage = stage;

        // --- Login Form Elements ---
        Label firstNameLabel = new Label("First Name:");
        firstNameField = new TextField();
        firstNameField.setPromptText("Enter your first name");

        Label lastNameLabel = new Label("Last Name:");
        lastNameField = new TextField();
        lastNameField.setPromptText("Enter your last name");
        Label marketID = new Label("Market ID:");
        TextField marketIDField = new TextField();


        loginButton = new Button("Login");
        loginButton.setDefaultButton(true);

        // --- Layout for Login Stage ---
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        root.getChildren().addAll(
                firstNameLabel,
                firstNameField,
                lastNameLabel,
                lastNameField,
                marketID,
                marketIDField,
                loginButton
        );

        // --- Event Handling for Login Button ---
        loginButton.setOnAction(event -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();

            int supermarketID = Integer.parseInt(marketIDField.getText().trim());

            if (!firstName.isEmpty() && !lastName.isEmpty() &&( databaseManager.employeeExists(firstName, lastName,supermarketID)) && !(databaseManager.isAdmin(firstName,lastName))) {
                System.out.println("Login Successful for as a cashier: " + firstName + " " + lastName);
                openMainForm(primaryStage);
            } else if (!firstName.isEmpty() && !lastName.isEmpty() &&( databaseManager.employeeExists(firstName, lastName,supermarketID)) && (databaseManager.isAdmin(firstName,lastName))) {
                System.out.println("Login Successful for as an admin: " + firstName + " " + lastName);
                openAdminForm(primaryStage);

            }else {
                System.out.println("Please enter both first and last name.");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Login Error");
                alert.setHeaderText(null);
                alert.setContentText("You do not have access to the cashier application.");
                alert.showAndWait();
            }
        });

        // --- Scene and Stage Setup ---
        Scene scene = new Scene(root, 350, 280);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    private void openMainForm(Stage currentStage) {
        try {
            // Close the current login stage
            currentStage.close();
            MainForm mainForm = new MainForm();
            Stage mainStage = new Stage();
            mainForm.start(mainStage);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to open Main Form.");
        }
    }
    private void openAdminForm(Stage currentStage) {
        try {

            currentStage.close();
            AdminForm adminForm = new AdminForm();
            Stage adminStage = new Stage();
            adminForm.start(adminStage);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to open Admin Form.");
        }
    }
    private void handleLogout() {
        Alert confirmLogout = new Alert(Alert.AlertType.CONFIRMATION);
        confirmLogout.setTitle("Logout Confirmation");
        confirmLogout.setHeaderText(null);
        confirmLogout.setContentText("Are you sure you want to logout?");

        confirmLogout.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Close the current MainForm stage
                primaryStage.close();

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

//    public static void main(String[] args) {
//        launch(args);
//    }
}