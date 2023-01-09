package com.enimplanner;

import java.io.IOException;
import javafx.fxml.FXML;

public class HomeController {

    @FXML
    private void Matieres() throws IOException {
        App.setRoot("matieres");
    }

    @FXML
    private void Todos() throws IOException {
        App.setRoot("todos");
    }

    @FXML
    private void Exams() throws IOException {
        App.setRoot("exams");
    }
}
