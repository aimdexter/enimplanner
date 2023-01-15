package com.enimplanner;

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

public class MatieresController implements Initializable {

    @FXML
    private TextField textCoefficient;
    @FXML
    private Label textUsername;
    @FXML
    private Label textTotalMat;
    @FXML
    private TableView textlistMat;
    @FXML
    private Button  btndel;
    @FXML
    private TextField textMat;
    @FXML
    private Button btnadd;
    @FXML
    private DatePicker textDateMatiere;
    @FXML
    private TextField textNomMatiere;

    @FXML
    private Button btnMatExamen;
    @FXML
    private Button btnMatProfile;
    @FXML
    private Button btnMatTodo;
    @FXML
    private Button btnMatLogout;


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
    String countMatiere = "SELECT COUNT(*) FROM matiere where id_etudiant = \'"+loggedInUserId+"';";
    String listmat = "SELECT id_matiere,nom_matiere,date_matiere , id_etudiant FROM matiere where id_etudiant = \'"+loggedInUserId+"';";
    String deltitem = "DELETE FROM matiere WHERE id_matiere = ?";
    String add = "INSERT INTO matiere (nom_matiere,date_matiere,coefficient , id_etudiant) VALUES (?,?,?,"+loggedInUserId+");";
    String update = "UPDATE matiere SET nom_matiere = ?, date_matiere = ? , coefficient = ? WHERE id_matiere = ? AND id_etudiant = \'"+loggedInUserId+"';";
    private Window owner;



    public MatieresController() {
        connection = ConnectionUtil.connectdb();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        fetColumnList();
        fetRowList();
        afficherValeurs();
    }

    //only fetch columns
    private void fetColumnList() {

        try {
            resultSet = connection.createStatement().executeQuery(listmat);

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
                textlistMat.setEditable(true);
                textlistMat.getColumns().removeAll(col);
                textlistMat.getColumns().addAll(col);

            }

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());

        }
    }

    //fetches rows and data from the list
    private void fetRowList() {
        data = FXCollections.observableArrayList();
        try {
            resultSet = connection.createStatement().executeQuery(listmat);

            while (resultSet.next()) {
                //Iterate Row
                ObservableList row = FXCollections.observableArrayList();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(resultSet.getString(i));
                }
                data.add(row);

            }
            textlistMat.setItems(data);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    //Delete rows and data from the list by id_matiere
    @FXML
    private void deleteAction(ActionEvent event) {

        String id_matiere = textMat.getText().toString();
        try {
            preparedStatement = connection.prepareStatement(deltitem);
            if (id_matiere.matches("^[0-9]*$") || id_matiere == "") {
                preparedStatement.setInt(1, Integer.parseInt(textMat.getText()));
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

    //Afficher les vdonnees de l utilisteur
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
            resultSet = Statement.executeQuery(countMatiere);
            if(resultSet.next()){
                int countmat = resultSet.getInt("count");
                textTotalMat.setText(""+countmat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    };


    @FXML
    private void addAction(ActionEvent event) throws IOException {
        if (textNomMatiere.getText().isEmpty() || textDateMatiere.getValue() == null || textCoefficient.getText().toString().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!","Veuillez renseigner tous les champs");
            return;
        }

        String Coefficient = textCoefficient.getText().toString();
        
        try {
            preparedStatement = connection.prepareStatement(add);
            preparedStatement.setString(1, textNomMatiere.getText().toString());
            preparedStatement.setDate(2, Date.valueOf(textDateMatiere.getValue()));

            if (Coefficient.matches("^[0-9]*$")) {
                preparedStatement.setInt(3, Integer.parseInt(Coefficient));
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
    };

    @FXML
    void btnSearch(ActionEvent event) {
    }
    
    @FXML
    void switchMatLogout(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        dialogStage = (Stage) source.getScene().getWindow();
        dialogStage.close();
        
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    @FXML
    void switchMatProfile(ActionEvent event) throws IOException {
    }

    // void switchMatTodo(ActionEvent event) throws IOException {
    //     Node source = (Node) event.getSource();
    //     dialogStage = (Stage) source.getScene().getWindow();
    //     dialogStage.close();
        
    //     Parent root = FXMLLoader.load(getClass().getResource("todos.fxml"));
    //     Scene scene = new Scene(root);
    //     dialogStage.setScene(scene);
    //     dialogStage.show();
    // }

    @FXML
    void switchMatExamen(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        dialogStage = (Stage) source.getScene().getWindow();
        dialogStage.close();
        
        Parent root = FXMLLoader.load(getClass().getResource("exams.fxml"));
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    @FXML
    void updateAction(ActionEvent event) {
        String Coefficient = textCoefficient.getText().toString();
        String id_matiere = textMat.getText().toString();

        if (textNomMatiere.getText().isEmpty() || textDateMatiere.getValue() == null || textCoefficient.getText().isEmpty() || textMat.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!","Veuillez renseigner tous les champs");
            return;
        }
       
        
        try {
            preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, textNomMatiere.getText().toString());
            preparedStatement.setDate(2, Date.valueOf(textDateMatiere.getValue()));

            if (id_matiere.matches("^[0-9]*$")) {
                preparedStatement.setInt(4, Integer.parseInt(id_matiere));
            }
            else{
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!","Id matiere doit etre un chiffre");
            }

            if (Coefficient.matches("^[0-9]*$")) {
                preparedStatement.setInt(3, Integer.parseInt(Coefficient));
            }
            else{
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!","Coefficient de la matiere doit etre un chiffre");
            }
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        afficherValeurs();
        fetRowList();
    };
    
    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

}
