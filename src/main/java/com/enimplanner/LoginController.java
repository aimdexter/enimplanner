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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML
    private TextField textEmail;
    
    @FXML
    private PasswordField textPassword;

    @FXML
    private Text textUsername;
    
    Stage dialogStage = new Stage();
    Scene scene;
    
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    public static String loggedInUserId;

    public LoginController() {
        connection = ConnectionUtil.connectdb();
    }

    @FXML
    private void loginAction(ActionEvent event) {
    String email = textEmail.getText().toString();
    String password = textPassword.getText().toString();
 
    String sql = "SELECT * FROM etudiant WHERE email = ? and password = ?";

    try{
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, password);
        resultSet = preparedStatement.executeQuery();
        if(!resultSet.next()){
            infoBox("Enter Correct Email and Password", "Failed", null);
        }else{
            loggedInUserId =  resultSet.getString("id_etudiant");
            Node source = (Node) event.getSource();
            dialogStage = (Stage) source.getScene().getWindow();
            dialogStage.close();
            
            Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.show();
        }
         
        }catch(Exception e){
            e.printStackTrace();
        }
    }

        public static void infoBox(String infoMessage, String titleBar, String headerMessage){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(titleBar);
            alert.setHeaderText(headerMessage);
            alert.setContentText(infoMessage);
            alert.showAndWait();
        }

    @FXML
    private void Signup(ActionEvent event) throws IOException{
        Node source = (Node) event.getSource();
        dialogStage = (Stage) source.getScene().getWindow();
        dialogStage.close();
        
        Parent root = FXMLLoader.load(getClass().getResource("signup.fxml"));
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        dialogStage.show();
}


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    }
}
