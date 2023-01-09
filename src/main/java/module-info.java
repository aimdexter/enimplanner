module com.enimplanner {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    opens com.enimplanner to javafx.fxml;
    exports com.enimplanner;
    requires java.sql;

}
