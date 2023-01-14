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
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import javafx.beans.property.SimpleStringProperty;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;

public class MatieresController implements Initializable {

    @FXML
    private Label textUsername;
    @FXML
    private Label textTotalMat;
    @FXML
    private TableView textlistMat;
    @FXML
    private Button  btndel;
    @FXML
    private TextField textSstatut;
    @FXML
    private Button btnadd;
    @FXML
    private DatePicker textDateMatiere;
    @FXML
    private TextField textNomMatiere;


    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Statement Statement = null;
    ResultSet resultSet = null;
    ResultSet resultSetMat = null;
    ObservableList<String> items = FXCollections.observableArrayList();
    private ObservableList<ObservableList> data;

    String sql = "SELECT * FROM etudiant WHERE id_etudiant = \'"+loggedInUserId+"';";
    String countMatiere = "SELECT COUNT(*) FROM matiere where id_etudiant = \'"+loggedInUserId+"';";
    String listmat = "SELECT * FROM matiere where id_etudiant = \'"+loggedInUserId+"';";
    String deltitem = "DELETE FROM matiere WHERE id_matiere = ?";
    String add = "INSERT INTO matiere (nom_matiere,date_matiere, id_etudiant) VALUES (?,?,"+loggedInUserId+");";
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
        try {
            preparedStatement = connection.prepareStatement(deltitem);
            preparedStatement.setInt(1, Integer.parseInt(textSstatut.getText()));
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
        if (textNomMatiere.getText().isEmpty() || textDateMatiere.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
            "Veuillez renseigner tous les champs");
            return;
        }

        String NomMatiere = textNomMatiere.getText().toString();
        LocalDate DateMatiere = textDateMatiere.getValue();
        
        try {
            preparedStatement = connection.prepareStatement(add);
            preparedStatement.setString(1, NomMatiere);
            preparedStatement.setDate(2, Date.valueOf(DateMatiere));
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
