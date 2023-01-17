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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;


public class MatieresController implements Initializable {

    @FXML
    private TextField textCoefficient;
    @FXML
    private Label textUsername;
    @FXML
    private Label textTotalMat;
    @FXML
    private Button  btndel;
    @FXML
    private TextField textMat;
    @FXML
    private Button btnadd;
    @FXML
    private Button btnrechercher;
    @FXML
    private TextField txtRecherche;
    @FXML
    private DatePicker textDateMatiere;
    @FXML
    private TextField textNomMatiere;
    @FXML
    private Button btnMatExamen;
    @FXML
    private Button Profile;
    @FXML
    private Button btnMatTodo;
    @FXML
    private Button btnMatLogout;
    @FXML
    private Button userTaches;


    @FXML
    private TableView<Matieres> textlistMat1;
    @FXML
    private TableColumn<Matieres, Date> col_Date_Mat;
    @FXML
    private TableColumn<Matieres, Integer> col_Id_Etud;
    @FXML
    private TableColumn<Matieres, String> col_Nom_Mat;
    @FXML
    private TableColumn<Matieres, Integer> col_Coef_Mat;
    @FXML
    private TableColumn<Matieres, Integer> col_id_Mat;


    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Statement Statement = null;
    ResultSet resultSet = null;
    ResultSet resultSetSearch = null;
    ResultSet resultSetMat = null;
    public ObservableList<Matieres> data2 = FXCollections.observableArrayList();

    Stage dialogStage = new Stage();
    Scene scene;


    String sql = "SELECT * FROM etudiant WHERE id_etudiant = \'"+loggedInUserId+"';";
    String countMatiere = "SELECT COUNT(*) FROM matiere where id_etudiant = \'"+loggedInUserId+"';";
    String listmat = "SELECT * FROM matiere where id_etudiant = \'"+loggedInUserId+"';";
    String deltitem = "DELETE FROM matiere WHERE id_matiere = ?";
    String add = "INSERT INTO matiere (nom_matiere,date_matiere,coefficient , id_etudiant) VALUES (?,?,?,"+loggedInUserId+");";
    String update = "UPDATE matiere SET nom_matiere = ?, date_matiere = ? , coefficient = ? WHERE id_matiere = ? AND id_etudiant = \'"+loggedInUserId+"';";
    private Window owner;




    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        afficherValeurs();
        afficherMatiere();
    }
   
    public MatieresController() {
        connection = ConnectionUtil.connectdb();
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
        afficherMatiere();
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


    public void afficherMatiere() {
        try {
            resultSet = connection.createStatement().executeQuery(listmat);
            data2.clear();
            while (resultSet.next()) {
                data2.add(new Matieres(resultSet.getInt("id_matiere"),
                resultSet.getString("nom_matiere"),
                resultSet.getDate("date_matiere"), 
                resultSet.getInt("coefficient"), 
                resultSet.getInt("id_etudiant")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        col_id_Mat.setCellValueFactory(new PropertyValueFactory<Matieres, Integer>("id_matiere"));
        col_Nom_Mat.setCellValueFactory(new PropertyValueFactory<Matieres, String>("nom_matiere"));
        col_Date_Mat.setCellValueFactory(new PropertyValueFactory<Matieres, Date>("date_matiere"));
        col_Coef_Mat.setCellValueFactory(new PropertyValueFactory<Matieres, Integer>("coefficient"));
        col_Id_Etud.setCellValueFactory(new PropertyValueFactory<Matieres, Integer>("id_etudiant"));
        textlistMat1.setItems(data2);

        FilteredList<Matieres> filteredData = new FilteredList<>(data2, b -> true);

        		// 2. Set the filter Predicate whenever the filter changes.
		txtRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(matiere -> {
				// If filter text is empty, display all matiere.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				// Compare first name of every matiere with filter text.
				String lowerCaseFilter = newValue.toLowerCase();
				
				if (matiere.getNom_matiere().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
					return true; // Filter matches first name.
				}return false; // Does not match.
			});
		});
        // 3. Wrap the FilteredList in a SortedList. 
		SortedList<Matieres> sortedData = new SortedList<>(filteredData);
	
		// 4. Bind the SortedList comparator to the TableView comparator.
		// 	  Otherwise, sorting the TableView would have no effect.
		sortedData.comparatorProperty().bind(textlistMat1.comparatorProperty());
		
		// 5. Add sorted (and filtered) data to the table.
		textlistMat1.setItems(sortedData);
    }

    @FXML
    void btnSearch(ActionEvent event)  {        
    }

    @FXML
    private void addAction(ActionEvent event) throws IOException {
        if (textNomMatiere.getText().isEmpty() || textDateMatiere.getValue() == null || textCoefficient.getText().toString().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!","Veuillez renseigner tous les champs");
            return;
        }

        String coefficient = textCoefficient.getText().toString();
        
        try {
            preparedStatement = connection.prepareStatement(add);
            preparedStatement.setString(1, textNomMatiere.getText().toString());
            preparedStatement.setDate(2, Date.valueOf(textDateMatiere.getValue()));

            if (coefficient.matches("^[0-9]*$")) {
                preparedStatement.setInt(3, Integer.parseInt(coefficient));
            }
            else{
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!","Veillez enter un chiffre");
                return;
            }
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
            "Cette matiere existe déjà");
            return;
        }
        afficherValeurs();
        afficherMatiere();
    };

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
    void Profile(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        dialogStage = (Stage) source.getScene().getWindow();
        dialogStage.close();
        
        Parent root = FXMLLoader.load(getClass().getResource("profile.fxml"));
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        dialogStage.show();
    }

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
        afficherMatiere();
    };
    
    @FXML
    void userTaches(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        dialogStage = (Stage) source.getScene().getWindow();
        dialogStage.close();
        
        Parent root = FXMLLoader.load(getClass().getResource("todos.fxml"));
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
