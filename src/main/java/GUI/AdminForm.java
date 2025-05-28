package GUI;

import Databases.DatabaseManager;
import Databases.dbObjects.Product;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class AdminForm extends Application {
    private Stage primaryStage;
    private VBox root;
    private DatabaseManager databaseManager;
    private TextField searchField;
    private Button searchButton;
    private TextArea resultArea;

    public void start(Stage stage) throws Exception {
        databaseManager = new DatabaseManager();
        this.primaryStage = stage;
        stage.setTitle("Admin Form");

        this.root = new VBox(20);
        root.setPadding(new Insets(20));

        Label searchLabel = new Label("sheiyvane productis saxeli");
        searchField = new TextField();
        searchField.setPromptText("Product name");
        searchButton = new Button("Search");

        resultArea = new TextArea();
        resultArea.setEditable(false);

        resultArea.setPrefHeight(150);
        HBox searchBox = new HBox(10);
        searchBox.getChildren().addAll(searchField, searchButton);

        root.getChildren().addAll(searchLabel, searchBox, resultArea);

        searchButton.setOnAction(e -> searchProduct());
        Scene scene = new Scene(root, 450, 350);
        stage.setTitle("Search");
        stage.setScene(scene);
        stage.show();
    }

    private void searchProduct() {
        String searchTerm = searchField.getText().trim();
        resultArea.clear();

        if (searchTerm.isEmpty()) {
            resultArea.setText("sheiyvane producti");
        }

        List<Product> products = DatabaseManager.searchProductsByName(searchTerm);

        if (products.isEmpty()) {
            resultArea.setText("ver vipovet produqti: " + searchTerm);
        } else {
            StringBuilder result = new StringBuilder();
            result.append("vipovet: ").append(searchTerm).append("\n\n");

            for (Product product : products) {
                result.append("Name: ").append(product.getName()).append("\n");
                result.append("fasi: $").append(String.format("%.2f", product.getPrice())).append("\n");
                result.append(" ID: ").append(product.getCategoryId()).append("\n");
                result.append("raodenoba: ").append(product.getQuantityInStock()).append("\n\n");
            }
            resultArea.setText(result.toString());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
