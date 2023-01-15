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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;

public class TodosController implements Initializable{

    @FXML
    private TextField Recherche;
    @FXML
    private Button btnExamens;
    @FXML
    private Button btnTodo;
    @FXML
    private Button btnadd;
    @FXML
    private Button btndel;
    @FXML
    private Button btnrechercher;
    @FXML
    private Button btnupdate;
    @FXML
    private DatePicker textDateExam;
    @FXML
    private TextField textExam;
    @FXML
    private TextField textNomTodo;
    @FXML
    private Label textTotalTodos;
    @FXML
    private Label textUsername;
    @FXML
    private TableView textlistTodos;
    @FXML
    private Button btnMatiere;

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
    String countTodos = "SELECT COUNT(*) FROM todo where id_etudiant = \'"+loggedInUserId+"';";
    String listetodos = "SELECT id_todo,nom_todo,date_todo , id_etudiant FROM todo where id_etudiant = \'"+loggedInUserId+"';";
    String deltitem = "DELETE FROM todo WHERE id_todo = ?";
    String add = "INSERT INTO todo (nom_todo, date_todo, id_etudiant) VALUES (?,?,"+loggedInUserId+");";
    String update = "UPDATE todo SET nom_todo = ?, date_todo = ? WHERE id_todo = ? AND id_etudiant = \'"+loggedInUserId+"';";
    private Window owner;

    public TodosController() {
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
            resultSet = Statement.executeQuery(countTodos);
            if(resultSet.next()){
                int counexam = resultSet.getInt("count");
                textTotalTodos.setText(""+counexam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    };

     //only fetch columns
     private void fetColumnList() {

        try {
            resultSet = connection.createStatement().executeQuery(listetodos);

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
                textlistTodos.setEditable(true);
                textlistTodos.getColumns().removeAll(col);
                textlistTodos.getColumns().addAll(col);

            }

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());

        }
    }

    //fetches rows and data from the list
    private void fetRowList() {
        data = FXCollections.observableArrayList();
        try {
            resultSet = connection.createStatement().executeQuery(listetodos);

            while (resultSet.next()) {
                //Iterate Row
                ObservableList row = FXCollections.observableArrayList();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(resultSet.getString(i));
                }
                data.add(row);

            }
            textlistTodos.setItems(data);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }


    @FXML
    private void addAction(ActionEvent event) throws IOException {
        if (textNomTodo.getText().isEmpty() || textDateExam.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!","Veuillez renseigner tous les champs");
            return;
        }
        try {
            preparedStatement = connection.prepareStatement(add);
            preparedStatement.setString(1, textNomTodo.getText().toString());
            preparedStatement.setDate(2, Date.valueOf(textDateExam.getValue()));
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        afficherValeurs();
        fetRowList();
    };


    @FXML
    void btnSearch(ActionEvent event) {

    }

    //Delete rows and data from the list by id_matiere
    @FXML
    private void deleteAction(ActionEvent event) {
        String id_mexam = textExam.getText().toString();
        try {
            preparedStatement = connection.prepareStatement(deltitem);
            if (id_mexam.matches("^[0-9]*$") || id_mexam == "") {
                preparedStatement.setInt(1, Integer.parseInt(textExam.getText()));
            }
            else{
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!","Veillez enter un chiffre");
            }

            preparedStatement.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        afficherValeurs();
        fetRowList();
    }


    @FXML
    void updateAction(ActionEvent event) {
        String id_todo = textExam.getText().toString();

        if (textNomTodo.getText().isEmpty() || textDateExam.getValue() == null || textExam.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!","Veuillez renseigner tous les champs");
            return;
        }
       
        
        try {
            preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, textNomTodo.getText().toString());
            preparedStatement.setDate(2, Date.valueOf(textDateExam.getValue()));

            if (id_todo.matches("^[0-9]*$")) {
                preparedStatement.setInt(3, Integer.parseInt(id_todo));
            }
            else{
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!","Id todo doit etre un chiffre");
            }
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        afficherValeurs();
        fetRowList();
    };


    
    @FXML
    void switchMatieres(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        dialogStage = (Stage) source.getScene().getWindow();
        dialogStage.close();
        
        Parent root = FXMLLoader.load(getClass().getResource("matieres.fxml"));
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    @FXML
    void switchExamens(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        dialogStage = (Stage) source.getScene().getWindow();
        dialogStage.close();
        
        Parent root = FXMLLoader.load(getClass().getResource("exams.fxml"));
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    @FXML
    void switchLogout(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        dialogStage = (Stage) source.getScene().getWindow();
        dialogStage.close();
        
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    @FXML
    void switchProfile(ActionEvent event) throws IOException {
    }


    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }



}
