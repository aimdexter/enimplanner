package com.enimplanner;

import java.io.IOException;
import javafx.fxml.FXML;

public class LoginController {

    @FXML
    private void Signup() throws IOException {
        App.setRoot("signup");
    }

    @FXML
    private void Seconnecter() throws IOException {
        App.setRoot("home");
    }
}
