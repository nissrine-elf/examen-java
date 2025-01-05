module com.example.javaexamen {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javaexamen to javafx.fxml;
    exports com.example.javaexamen;
}