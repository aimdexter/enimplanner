package com.enimplanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Window;

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
    private Button btnMatiere;
    @FXML
    private TableColumn<Todos, Date> col_Date_Tache;
    @FXML
    private TableColumn<Todos, Integer> col_Id_Etudiant;
    @FXML
    private TableColumn<Todos, String> col_Nom_Tache;
    @FXML
    private TableColumn<Todos, Integer> col_id_Tache;
    @FXML
    private TableView<Todos> textlistTodos;


    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Statement Statement = null;
    ResultSet resultSet = null;
    ResultSet resultSetMat = null;
    public ObservableList<Todos> data = FXCollections.observableArrayList();
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

        afficherExamen();
        afficherValeurs();
    }

    public void afficherExamen() {
        try {
            resultSet = connection.createStatement().executeQuery(listetodos);
            data.clear();
            while (resultSet.next()) {
                data.add(new Todos(resultSet.getInt("id_todo"),
                resultSet.getString("nom_todo"),
                resultSet.getDate("date_todo"), 
                resultSet.getInt("id_etudiant")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        col_id_Tache.setCellValueFactory(new PropertyValueFactory<Todos, Integer>("id_todo"));
        col_Nom_Tache.setCellValueFactory(new PropertyValueFactory<Todos, String>("nom_todo"));
        col_Date_Tache.setCellValueFactory(new PropertyValueFactory<Todos, Date>("date_todo"));
        col_Id_Etudiant.setCellValueFactory(new PropertyValueFactory<Todos, Integer>("id_etudiant"));

        textlistTodos.setItems(data);

        FilteredList<Todos> filteredData = new FilteredList<>(data, b -> true);

                // 2. Set the filter Predicate whenever the filter changes.
        Recherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(exam -> {
                // If filter text is empty, display all matiere.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name of every matiere with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                
                if (exam.getNom_todo().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true; // Filter matches first name.
                }return false; // Does not match.
            });
        });
        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Todos> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(textlistTodos.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        textlistTodos.setItems(sortedData);

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
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
            "Cette tache existe déjà");
            return;
        }
        afficherValeurs();
        afficherExamen();
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
            if (id_mexam.matches("^[0-9]*$")) {
                preparedStatement.setInt(1, Integer.parseInt(textExam.getText()));
            }
            else{
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!","Veillez enter un chiffre");
                return;
            }
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!","Le champs id Tache ne peut pas etre vide !!");
        }
        afficherValeurs();
        afficherExamen();
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
        afficherExamen();
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
        Node source = (Node) event.getSource();
        dialogStage = (Stage) source.getScene().getWindow();
        dialogStage.close();
        
        Parent root = FXMLLoader.load(getClass().getResource("profile.fxml"));
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        dialogStage.show();
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
