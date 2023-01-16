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


public class ExamsController implements Initializable {

        @FXML
        private TextField txt_Exams_Recherche;
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
        private TextField textNomExam;
        @FXML
        private Label textTotalExam;
        @FXML
        private Label textUsername;
        @FXML
        private TableView<Exams> textlistExam;
        @FXML
        private TableColumn<Exams, Date> col_Date_Exam;
        @FXML
        private TableColumn<Exams, Integer> col_Id_Etud;
        @FXML
        private TableColumn<Exams, String> col_Nom_Exam;
        @FXML
        private TableColumn<Exams, Integer> col_id_Exam;
        @FXML
        private Button btnExamLogout;
    
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Statement Statement = null;
    ResultSet resultSet = null;
    ResultSet resultSetMat = null;
    public ObservableList<Exams> dataExams = FXCollections.observableArrayList();

    Stage dialogStage = new Stage();
    Scene scene;


    String sql = "SELECT * FROM etudiant WHERE id_etudiant = \'"+loggedInUserId+"';";
    String countExams = "SELECT COUNT(*) FROM exam where id_etudiant = \'"+loggedInUserId+"';";
    String listexams = "SELECT id_exam,nom_exam,date_exam , id_etudiant FROM exam where id_etudiant = \'"+loggedInUserId+"';";
    String deltitem = "DELETE FROM exam WHERE id_exam = ?";
    String add = "INSERT INTO exam (nom_exam, date_exam, id_etudiant) VALUES (?,?,"+loggedInUserId+");";
    String update = "UPDATE exam SET nom_exam = ?, date_exam = ? WHERE id_exam = ? AND id_etudiant = \'"+loggedInUserId+"';";
    private Window owner;

    public ExamsController() {
        connection = ConnectionUtil.connectdb();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        afficherMatiere();
        afficherValeurs();
    }

    public void afficherMatiere() {
        try {
            resultSet = connection.createStatement().executeQuery(listexams);
            dataExams.clear();
            while (resultSet.next()) {
                dataExams.add(new Exams(resultSet.getInt("id_exam"),
                resultSet.getString("nom_exam"),
                resultSet.getDate("date_exam"), 
                resultSet.getInt("id_exam")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        col_id_Exam.setCellValueFactory(new PropertyValueFactory<Exams, Integer>("id_exam"));
        col_Nom_Exam.setCellValueFactory(new PropertyValueFactory<Exams, String>("nom_exam"));
        col_Date_Exam.setCellValueFactory(new PropertyValueFactory<Exams, Date>("date_exam"));
        col_Id_Etud.setCellValueFactory(new PropertyValueFactory<Exams, Integer>("id_exam"));

        textlistExam.setItems(dataExams);

        
        FilteredList<Exams> filteredData = new FilteredList<>(dataExams, b -> true);

        		// 2. Set the filter Predicate whenever the filter changes.
		txt_Exams_Recherche.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(exam -> {
				// If filter text is empty, display all matiere.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				// Compare first name of every matiere with filter text.
				String lowerCaseFilter = newValue.toLowerCase();
				
				if (exam.getNom_exam().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
					return true; // Filter matches first name.
				}return false; // Does not match.
			});
		});
        // 3. Wrap the FilteredList in a SortedList. 
		SortedList<Exams> sortedData = new SortedList<>(filteredData);
	
		// 4. Bind the SortedList comparator to the TableView comparator.
		// 	  Otherwise, sorting the TableView would have no effect.
		sortedData.comparatorProperty().bind(textlistExam.comparatorProperty());
		
		// 5. Add sorted (and filtered) data to the table.
		textlistExam.setItems(sortedData);


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


    @FXML
    private void addAction(ActionEvent event) throws IOException {
        if (textNomExam.getText().isEmpty() || textDateExam.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!","Veuillez renseigner tous les champs");
            return;
        }
        try {
            preparedStatement = connection.prepareStatement(add);
            preparedStatement.setString(1, textNomExam.getText().toString());
            preparedStatement.setDate(2, Date.valueOf(textDateExam.getValue()));
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
            "Cet examen existe déjà");
            return;
        }
        afficherValeurs();
        afficherMatiere();
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
        afficherMatiere();
    }


    @FXML
    void updateAction(ActionEvent event) {
        String id_exam = textExam.getText().toString();

        if (textNomExam.getText().isEmpty() || textDateExam.getValue() == null || textExam.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!","Veuillez renseigner tous les champs");
            return;
        }
       
        
        try {
            preparedStatement = connection.prepareStatement(update);
            preparedStatement.setString(1, textNomExam.getText().toString());
            preparedStatement.setDate(2, Date.valueOf(textDateExam.getValue()));

            if (id_exam.matches("^[0-9]*$")) {
                preparedStatement.setInt(3, Integer.parseInt(id_exam));
            }
            else{
                showAlert(Alert.AlertType.ERROR, owner, "Form Error!","Id examen doit etre un chiffre");
            }
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        afficherValeurs();
        afficherMatiere();
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
    void switchExamLogout(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        dialogStage = (Stage) source.getScene().getWindow();
        dialogStage.close();
        
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        dialogStage.show();
    }



    @FXML
    void switchExamTodo(ActionEvent event) throws IOException {
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
