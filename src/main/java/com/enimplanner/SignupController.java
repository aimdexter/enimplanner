package com.enimplanner;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignupController implements Initializable{

    @FXML
    private Button home;

    @FXML
    private TextField textEmail;

    @FXML
    private TextField textNom;

    @FXML
    private TextField textOption;

    @FXML
    private PasswordField textPassword;

    @FXML
    private TextField textPrenom;



    Stage dialogStage = new Stage();
    Scene scene;
    
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public SignupController() {
        connection = ConnectionUtil.connectdb();
    }

    @FXML
    public void Home(ActionEvent event) throws IOException {

        Window owner = home.getScene().getWindow();

        
     
        String sql = "INSERT INTO etudiant (nom, prenom, niveau, email, password) VALUES (?, ?, ? , ?, ?)";

        if (textNom.getText().isEmpty() || textPrenom.getText().isEmpty() || textOption.getText().isEmpty() || textOption.getText().isEmpty() || textEmail.getText().isEmpty() || textPassword.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
            "Veuillez renseigner tous les champs");
            return;
        }

        String nom = textNom.getText().toString();
        String prenom = textPrenom.getText().toString();
        String option = textOption.getText().toString();
        String email = textEmail.getText().toString();
        String password = textPassword.getText().toString();   
        
        try {
            
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);
            preparedStatement.setString(3, option);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, password);

            preparedStatement.executeUpdate();
            
            Node source = (Node) event.getSource();
            dialogStage = (Stage) source.getScene().getWindow();
            dialogStage.close();
            
            Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.show();            

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
            "Cet Email existe déjà");
            return;
        }

        
    }

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // TODO Auto-generated method stub
    }
}
