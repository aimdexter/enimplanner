package com.enimplanner;

import static com.enimplanner.LoginController.loggedInUserId;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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

    Connection connection = null;
    private Statement preparedStatement = null;
    ResultSet resultSet = null;
    ResultSet resultSetMat = null;
    ObservableList<String> items = FXCollections.observableArrayList();
    private ObservableList<ObservableList> data;

    String sql = "SELECT * FROM etudiant WHERE id_etudiant = \'"+loggedInUserId+"';";
    String countMatiere = "SELECT COUNT(*) FROM matiere where id_etudiant = \'"+loggedInUserId+"';";
    String listmat = "SELECT * FROM matiere where id_etudiant = \'"+loggedInUserId+"';";
    String deltitem = "DELETE FROM matiere WHERE id_matiere = ?";


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
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(deltitem);
            preparedStatement.setInt(1, Integer.parseInt(textSstatut.getText()));
            preparedStatement.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        fetRowList();
    }

    //Afficher les vdonnees de l utilisteur
    private void afficherValeurs() {
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

    };
}
