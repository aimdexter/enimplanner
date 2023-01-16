package com.enimplanner;

import static com.enimplanner.LoginController.loggedInUserId;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ProfileController implements Initializable {

    @FXML
    private Label textUsername;

    @FXML
    private TextField txtemail;

    @FXML
    private TextField txtniveau;

    @FXML
    private TextField txtnom;

    @FXML
    private TextField txtpassword;

    @FXML
    private TextField txtprenom;

    @FXML
    private Button update;

    @FXML
    private Label useremail;

    @FXML
    private Label usermdp;

    @FXML
    private Label usernom;

    @FXML
    private Label useroption;

    @FXML
    private Label userprenom;
                             
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Statement Statement = null;
    ResultSet resultSet = null;
    Stage dialogStage = new Stage();
    Scene scene;


    String sql = "SELECT * FROM etudiant WHERE id_etudiant = \'"+loggedInUserId+"';";
    String updateprofile = "UPDATE etudiant SET nom = ?, prenom = ? , niveau = ?, password = ? WHERE id_etudiant = \'"+loggedInUserId+"';";
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // afficherValeurs();
    }
   
    public ProfileController() {
        connection = ConnectionUtil.connectdb();
    }


    //Afficher les vdonnees de l utilisteur
    // private void afficherValeurs() {
    //     try {
    //         Statement = connection.createStatement();
    //         resultSet = Statement.executeQuery(sql);
    //         if(resultSet.next()){
    //             String nom = resultSet.getString("nom").toUpperCase();
    //             String prenom = resultSet.getString("prenom").toUpperCase();
    //             String Username = nom +" "+ prenom ;
    //             textUsername.setText(Username);
    //             usernom.setText(nom);
    //             userprenom.setText(prenom);
    //             useroption.setText(resultSet.getString("niveau").toUpperCase());
    //             useremail.setText(resultSet.getString("email").toUpperCase());
    //             usermdp.setText(resultSet.getString("password").toUpperCase());

    //             textUsername.setText(Username);

    //             txtnom.setText(nom);
    //             txtprenom.setText(prenom);
    //             txtniveau.setText(resultSet.getString("niveau").toUpperCase());
    //             useremail.setText(resultSet.getString("email").toUpperCase());
    //             txtpassword.setText(resultSet.getString("password").toUpperCase());
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // };


    
    // @FXML
    // void updateData(ActionEvent event) {      
    //     try {
    //         preparedStatement = connection.prepareStatement(updateprofile);
    //         preparedStatement.setString(1, txtnom.getText().toString());
    //         preparedStatement.setString(2, txtprenom.getText().toString());
    //         preparedStatement.setString(3, txtniveau.getText().toString());
    //         preparedStatement.setString(4, txtpassword.getText().toString());
            
    //         System.out.println(preparedStatement);
    //         preparedStatement.executeUpdate();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     afficherValeurs();
    // };
    
}
