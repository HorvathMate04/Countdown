module org.example.countdown {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.countdown to javafx.fxml;
    exports org.example.countdown;
}