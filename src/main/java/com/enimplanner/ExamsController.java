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
        private Button profile;
        @FXML
        private Button btnExamens;
            // Cette variable fait référence à un bouton dans l'interface utilisateur de l'application, lorsque l'utilisateur clique sur le bouton, il exécutera l'action associée à celui-ci.         
        @FXML
        private Button btnTodo;
            // Cette variable fait référence à un bouton dans l'interface utilisateur de l'application, lorsque l'utilisateur clique sur le bouton, il exécutera l'action associée à celui-ci. 
        @FXML
        private Button btnadd;
            // Cette variable fait référence à un bouton dans l'interface utilisateur de l'application, lorsque l'utilisateur clique sur le bouton, il exécutera l'action associée à celui-ci. 
        @FXML
        private Button btndel;
            // Cette variable fait référence à un bouton dans l'interface utilisateur de l'application, lorsque l'utilisateur clique sur le bouton, il exécutera l'action associée à celui-ci. 
        @FXML
        private Button btnrechercher;
            // Cette variable fait référence à un bouton dans l'interface utilisateur de l'application, lorsque l'utilisateur clique sur le bouton, il exécutera l'action associée à celui-ci. 
        @FXML
        private Button btnupdate;
            // Cette variable fait référence à un bouton dans l'interface utilisateur de l'application, lorsque l'utilisateur clique sur le bouton, il exécutera l'action associée à celui-ci. 
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

        //La variable "col_Date_Exam" est de type "TableColumn<Exams, Date>" où "Exams" est une classe qui représente les données affichées dans la table, et "Date" est le type de données de la colonne.
        // Cette colonne de la table affichera les données de la propriété "date_exam" de la classe "Exams" qui est de type date.
        // Cette variable sera utilisée pour définir la colonne qui affiche les dates des examens dans la table. Elle peut être utilisée pour définir les propriétés de la colonne, telles que la largeur, la mise en forme des données affichées, et les événements liés à cette colonne.
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
            // Cette variable fait référence à un bouton dans l'interface utilisateur de l'application, lorsque l'utilisateur clique sur le bouton, il exécutera l'action associée à celui-ci.     
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Statement Statement = null;
        ResultSet resultSet = null;
        ResultSet resultSetMat = null;

        // Cette ligne de code en JavaFX déclare une variable "dataExams" de type "ObservableList<Exams>" qui va stocker des objets de type "Exams" pour les afficher dans une table de la vue.
        // "ObservableList" est une interface fournie par JavaFX qui étend la classe List de Java standard pour permettre la mise à jour automatique des éléments de la liste dans la vue.
        // "FXCollections.observableArrayList()" est une méthode qui retourne une instance d'une classe implémentant "ObservableList" qui est une liste observable d'objets.
        // Cette variable "dataExams" contiendra les objets de type "Exams" qui seront utilisés pour remplir les données de la table dans la vue. Cela permet de mettre à jour automatiquement la table lorsque les données sont modifiées.
        public ObservableList<Exams> dataExams = FXCollections.observableArrayList();

        //La variable "dialogStage" de type "Stage" qui est utilisée pour créer une nouvelle fenêtre de dialogue.
        Stage dialogStage = new Stage();
        Scene scene;


        //sélectionne toutes les colonnes pour un étudiant spécifique en fonction de son "id_etudiant"
        String sql = "SELECT * FROM etudiant WHERE id_etudiant = \'"+loggedInUserId+"';";

        //compte combien d'examens l'étudiant a.
        String countExams = "SELECT COUNT(*) FROM exam where id_etudiant = \'"+loggedInUserId+"';";

        //sélectionne les id, nom, date et l'id de l'étudiant pour tous les examens de l'étudiant spécifique.
        String listexams = "SELECT id_exam,nom_exam,date_exam , id_etudiant FROM exam where id_etudiant = \'"+loggedInUserId+"';";
        
        // supprime un examen spécifique en fonction de son "id_exam"
        String deltitem = "DELETE FROM exam WHERE id_exam = ?";

        // ajoute un nouvel examen pour l'étudiant connecté
        String add = "INSERT INTO exam (nom_exam, date_exam, id_etudiant) VALUES (?,?,"+loggedInUserId+");";
        
        // met à jour un examen spécifique pour l'étudiant connecté en modifiant le nom et la date de l'examen en fonction de son "id_exam"
        String update = "UPDATE exam SET nom_exam = ?, date_exam = ? WHERE id_exam = ? AND id_etudiant = \'"+loggedInUserId+"';";
        
        //Cette ligne de code déclare une variable privée nommée "owner" qui est de type "Window" dans la classe actuelle. "Window" est une classe de JavaFX qui représente une fenêtre graphique dans une application. La variable "owner" est utilisée pour stocker une référence à la fenêtre propriétaire de la fenêtre actuelle. Cela peut être utilisé pour spécifier la fenêtre parente lors de l'affichage de boîtes de dialogue ou de fenêtres contextuelles, ou pour effectuer des opérations telles que fermer la fenêtre parente lorsque la fenêtre actuelle est fermée.
        private Window owner;

    //Un constructeur est une méthode spéciale qui est appelée lorsqu'une nouvelle instance d'une classe est créée. Il est utilisé pour initialiser les propriétés ou les états de l'objet.
    //Dans ce cas précis, lorsqu'une nouvelle instance de la classe ExamsController est créée, le constructeur appelle la méthode "connectdb()" de la classe "ConnectionUtil" pour établir une connexion à une base de données, et stocke cette connexion dans la variable "connection". Cette connexion pourra être utilisée pour exécuter des requêtes à la base de données dans d'autres méthodes de la classe ExamsController.
    public ExamsController() {
        // La variable "connection" est de type "Connection" qui est une interface JDBC qui permet de connecter à une base de données.
        // La méthode "connectdb()" est supposée se charger de la configuration nécessaire pour établir une connexion avec une base de données, comme les informations d'identification pour se connecter à un serveur de base de données.
        // Une fois la connexion établie, elle pourra être utilisée pour exécuter des requêtes (select, insert, update, delete) à la base de données.
        connection = ConnectionUtil.connectdb();
    }

    //Ce code est une méthode de la classe actuelle qui surcharge (override) la méthode "initialize" de la classe parente. La méthode "initialize" est une méthode spéciale de JavaFX qui est automatiquement appelée lorsque la vue (ou la scène) associée à cette classe est chargée.
    // Les arguments "URL arg0" et "ResourceBundle arg1" sont des paramètres de la méthode d'initialisation fournis par JavaFX, ils peuvent être utilisés pour accéder à des ressources externes comme les fichiers FXML ou les fichiers de propriétés.
    // La méthode "initialize" contient deux appels de fonction :
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //cette fonction est utilisée pour afficher les examen.
        afficherMatiere();
        //cette fonction est utilisée pour afficher les valeurs.
        afficherValeurs();
    }

    public void afficherMatiere() {
        try {
            //La méthode commence par créer une requête SQL en utilisant la variable "listexams" qui contient une requête de sélection pour sélectionner toutes les examen. La requête est exécutée en utilisant l'objet "connection" qui est probablement une connexion à une base de données. Le résultat de la requête est stocké dans une variable "resultSet".
            resultSet = connection.createStatement().executeQuery(listexams);
            dataExams.clear();

            //La boucle "while (resultSet.next())" est utilisée pour parcourir les lignes de résultat de la requête. Pour chaque ligne, un nouvel objet "Exams" est créé en utilisant les valeurs des colonnes "id_exam", "nom_exam", "date_exam" et "id_exam" de la ligne en cours. Cet objet est ensuite ajouté à une collection appelée "dataExams" qui est utilisée pour stocker les objets "Exams" de toutes les lignes de résultat.
            while (resultSet.next()) {
                //Ensuite, la méthode définit les valeurs des propriétés "col_id_Exam", "col_Nom_Exam", "col_Date_Exam" et "col_Id_Etud" pour les cellules de la table "textlistExam", en utilisant les propriétés "id_exam", "nom_exam", "date_exam" et "id_exam" de la classe Exams respectivement.
                dataExams.add(new Exams(resultSet.getInt("id_exam"),
                resultSet.getString("nom_exam"),
                resultSet.getDate("date_exam")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Ce code est utilisé pour remplir une table graphique de JavaFX à partir d'une collection de données. Il définit les propriétés de la table pour chaque colonne en utilisant la méthode "setCellValueFactory".

        //cette ligne de code définit la propriété "col_id_Exam" pour utiliser la propriété "id_exam" de la classe "Exams" pour remplir les cellules de cette colonne.
        col_id_Exam.setCellValueFactory(new PropertyValueFactory<Exams, Integer>("id_exam"));

        //cette ligne de code définit la propriété "col_Nom_Exam" pour utiliser la propriété "nom_exam" de la classe "Exams" pour remplir les cellules de cette colonne.
        col_Nom_Exam.setCellValueFactory(new PropertyValueFactory<Exams, String>("nom_exam"));

        //cette ligne de code définit la propriété "col_Date_Exam" pour utiliser la propriété "date_exam" de la classe "Exams" pour remplir les cellules de cette colonne.
        col_Date_Exam.setCellValueFactory(new PropertyValueFactory<Exams, Date>("date_exam"));

        //cette ligne de code définit la propriété "col_Id_Etud" pour utiliser la propriété "id_exam" de la classe "Exams" pour remplir les cellules de cette colonne.
        // col_Id_Etud.setCellValueFactory(new PropertyValueFactory<Exams, Integer>("id_exam"));

        //Enfin, "textlistExam.setItems(dataExams);" est utilisé pour assigner la collection "dataExams" à la table "textlistExam" pour afficher les examen dans la table.
        textlistExam.setItems(dataExams);

        
        //La recherche est effectuée en utilisant une FilteredList qui contient les examens, cette liste est liée à un champ de recherche pour permettre la saisie de l'utilisateur.
        // filteredData = new FilteredList<>(dataExams, b -> true);" crée une nouvelle instance de "FilteredList" qui contient tous les éléments de la liste "dataExams" car la fonction de filtrage est définie comme "b -> true" qui signifie qu'elle ne filtre rien.
        FilteredList<Exams> filteredData = new FilteredList<>(dataExams, b -> true);

        //permet de surveiller les changements de texte dans un champ de recherche nommé "txt_Exams_Recherche", lorsque l'utilisateur entre du texte dans ce champ, la fonction anonyme suivante sera appelée.
		txt_Exams_Recherche.textProperty().addListener((observable, oldValue, newValue) -> {
            //La fonction anonyme utilise la méthode "setPredicate" pour définir un prédicat pour la liste filtrée qui vérifie si le nom de la matière contient le texte de recherche en minuscules. Si c'est le cas, l'élément est conservé dans la liste filtrée, sinon il est supprimé.
            // Si la valeur de recherche est nulle ou vide, tous les éléments seront ajoutés à la liste filtrée.
			filteredData.setPredicate(exam -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				
				if (exam.getNom_exam().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
					return true; 
				}return false;
			});
		});

        // La méthode utilise enfin une SortedList pour trier les examen filtrées par ordre alphabétique.
		SortedList<Exams> sortedData = new SortedList<>(filteredData);
	
        //"sortedData" est une instance de la classe "SortedList" qui contient les données filtrées de la table "textlistExam"
        // La méthode "comparatorProperty()" est utilisée pour accéder à la propriété de tri de la SortedList "sortedData"
        // La méthode "bind" est utilisée pour lier la propriété de tri de la SortedList "sortedData" à la propriété de tri de la table "textlistExam" en utilisant "textlistExam.comparatorProperty()".
		sortedData.comparatorProperty().bind(textlistExam.comparatorProperty());
		
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
    void goprofile(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        dialogStage = (Stage) source.getScene().getWindow();
        dialogStage.close();
        
        Parent root = FXMLLoader.load(getClass().getResource("profile.fxml"));
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
