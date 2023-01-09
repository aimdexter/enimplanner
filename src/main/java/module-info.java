module com.enimplanner {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.enimplanner to javafx.fxml;
    exports com.enimplanner;
}
