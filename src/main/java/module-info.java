module com.example.testeri {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.compiler;


    opens Core to javafx.fxml;
    exports Core;
//    exports Core;
//    opens Core to javafx.fxml;
}