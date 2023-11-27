module com.example.finalproject {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires mysql.connector.j;

    opens com.example.finalproject to javafx.fxml;
    exports com.example.finalproject;
}