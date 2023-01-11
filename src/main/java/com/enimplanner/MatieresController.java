package com.enimplanner;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import static com.enimplanner.LoginController.loggedInUserId;

public class MatieresController implements Initializable {

    Connection connection = null;
    private Statement preparedStatement = null;
    ResultSet resultSet = null;
    ResultSet resultSetMat = null;

    public MatieresController() {
        connection = ConnectionUtil.connectdb();
    }

    @FXML
    private Label textUsername;

    @FXML
    private Label textTotalMat;

    @FXML
    private ListView<String> textlistMat;

    ObservableList<String> items = FXCollections.observableArrayList();

    String sql = "SELECT * FROM etudiant WHERE id_etudiant = \'"+loggedInUserId+"';";
    String countMatiere = "SELECT COUNT(*) FROM matiere where id_etudiant = \'"+loggedInUserId+"';";
    String listmat = "SELECT * FROM matiere where id_etudiant = \'"+loggedInUserId+"';";


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
        try {
            preparedStatement = connection.createStatement();
            resultSet = preparedStatement.executeQuery(sql);
            if(resultSet.next()){
                String nom = resultSet.getString("nom").toUpperCase();
                String prenom = resultSet.getString("prenom").toUpperCase();
                String Username = nom +" "+ prenom ;
                textUsername.setText(Username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            preparedStatement = connection.createStatement();
            resultSet = preparedStatement.executeQuery(countMatiere);
            if(resultSet.next()){
                int countmat = resultSet.getInt("count");
                textTotalMat.setText(""+countmat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            preparedStatement = connection.createStatement();
            resultSet = preparedStatement.executeQuery(listmat);
            while (resultSet.next()) {
                textlistMat.setItems(items);
                String nom_matiere = resultSet.getString("nom_matiere").toUpperCase();
                String date_matiere = resultSet.getString("date_matiere").toUpperCase();
                String id_etudiant = resultSet.getString("id_etudiant").toUpperCase();
                String matieres = nom_matiere +" "+ date_matiere+" "+ id_etudiant ;
                items.add(matieres);
            }
    } catch (SQLException e) {
            e.printStackTrace();
        }
        

    }
    
}
