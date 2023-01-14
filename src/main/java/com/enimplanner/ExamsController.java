package com.enimplanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import static com.enimplanner.LoginController.loggedInUserId;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;


public class ExamsController implements Initializable {

        @FXML
        private TextField Recherche;
    
        @FXML
        private Button btnExamens;
    
        @FXML
        private Button btnMenus;
    
        @FXML
        private Button btnOrders;
    
        @FXML
        private Button btnOverview;
    
        @FXML
        private Button btnPackages;
    
        @FXML
        private Button btnadd;
    
        @FXML
        private Button btndel;
    
        @FXML
        private Button btnrechercher;
    
        @FXML
        private Button btnupdate;
    
        @FXML
        private Pane pnlCustomer;
    
        @FXML
        private Pane pnlMenus;
    
        @FXML
        private Pane pnlOrders;
    
        @FXML
        private Pane pnlOverview;
    
        @FXML
        private DatePicker textDateExam;
    
        @FXML
        private TextField textExam;
    
        @FXML
        private TextField textNomExam;
    
        @FXML
        private Label textTotalExam;
    
        @FXML
        private Label textUsername;
    
        @FXML
        private Label textUsername11;
    
        @FXML
        private TableView textlistExam;
    
        @FXML
        void addAction(ActionEvent event) {
    
        }
    
        @FXML
        void btnSearch(ActionEvent event) {
    
        }
    
        @FXML
        void deleteAction(ActionEvent event) {
    
        }
    
        @FXML
        void updateAction(ActionEvent event) {
    
        }
    
    
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Statement Statement = null;
    ResultSet resultSet = null;
    ResultSet resultSetMat = null;
    ObservableList<String> items = FXCollections.observableArrayList();
    private ObservableList<ObservableList> data;
    Stage dialogStage = new Stage();
    Scene scene;


    String sql = "SELECT * FROM etudiant WHERE id_etudiant = \'"+loggedInUserId+"';";
    String countExams = "SELECT COUNT(*) FROM exam where id_etudiant = \'"+loggedInUserId+"';";
    String listexams = "SELECT id_exam,nom_exam,date_exam , id_etudiant FROM exam where id_etudiant = \'"+loggedInUserId+"';";
    String deltitem = "DELETE FROM matiere WHERE id_matiere = ?";
    String add = "INSERT INTO matiere (nom_matiere,date_matiere,coefficient , id_etudiant) VALUES (?,?,?,"+loggedInUserId+");";
    String update = "UPDATE matiere SET nom_matiere = ?, date_matiere = ? , coefficient = ? WHERE id_matiere = ? AND id_etudiant = \'"+loggedInUserId+"';";
    private Window owner;

    public ExamsController() {
        connection = ConnectionUtil.connectdb();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        fetColumnList();
        fetRowList();
        afficherValeurs();
    }

    private void afficherValeurs() {
        try {
            Statement = connection.createStatement();
            resultSet = Statement.executeQuery(sql);
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
            Statement = connection.createStatement();
            resultSet = Statement.executeQuery(countExams);
            if(resultSet.next()){
                int counexam = resultSet.getInt("count");
                textTotalExam.setText(""+counexam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    };



     //only fetch columns
     private void fetColumnList() {

        try {
            resultSet = connection.createStatement().executeQuery(listexams);

            //SQL FOR SELECTING ALL OF MATIERE
            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(resultSet.getMetaData().getColumnName(i + 1).toUpperCase());
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                textlistExam.setEditable(true);
                textlistExam.getColumns().removeAll(col);
                textlistExam.getColumns().addAll(col);

            }

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());

        }
    }

    //fetches rows and data from the list
    private void fetRowList() {
        data = FXCollections.observableArrayList();
        try {
            resultSet = connection.createStatement().executeQuery(listexams);

            while (resultSet.next()) {
                //Iterate Row
                ObservableList row = FXCollections.observableArrayList();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(resultSet.getString(i));
                }
                data.add(row);

            }
            textlistExam.setItems(data);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
