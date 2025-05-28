
package Core;
import Databases.DatabaseManager;
import GUI.AdminForm;
import GUI.Login;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Login loginWindow = new Login();
        loginWindow.start(stage);



    }
    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();
        Connection dbConnection = DatabaseManager.getConnection();

        if (dbConnection != null) {
            System.out.println("connected to database successfully.");
        } else {
            System.out.println("Could not establish database connection.");
        }
        launch();
    }
}