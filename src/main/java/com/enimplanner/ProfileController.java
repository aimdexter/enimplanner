package com.enimplanner;

import static com.enimplanner.LoginController.loggedInUserId;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ProfileController implements Initializable {

    //Cette variable est utilisée pour faire référence à un champ de texte dans l'interface utilisateur de l'application, qui est utilisé pour accepter une entrée
    @FXML
    private Label textUsername;
    //Cette variable est utilisée pour faire référence à un champ de texte dans l'interface utilisateur de l'application, qui est utilisé pour accepter une entrée
    @FXML
    private TextField txtemail;
    //Cette variable est utilisée pour faire référence à un champ de texte dans l'interface utilisateur de l'application, qui est utilisé pour accepter une entrée
    @FXML
    private TextField txtniveau;
    //Cette variable est utilisée pour faire référence à un champ de texte dans l'interface utilisateur de l'application, qui est utilisé pour accepter une entrée
    @FXML
    private TextField txtnom;
    //Cette variable est utilisée pour faire référence à un champ de texte dans l'interface utilisateur de l'application, qui est utilisé pour accepter une entrée
    @FXML
    private TextField txtpassword;
    //Cette variable est utilisée pour faire référence à un champ de texte dans l'interface utilisateur de l'application, qui est utilisé pour accepter une entrée
    @FXML
    private TextField txtprenom;
    // Cette variable fait référence à un bouton dans l'interface utilisateur de l'application, lorsque l'utilisateur clique sur le bouton, il exécutera l'action associée à celui-ci. 
    @FXML
    private Button update;
    //Cette variable est utilisée pour faire référence à un champ de texte dans l'interface utilisateur de l'application, qui est utilisé pour accepter une entrée
    @FXML
    private Label useremail;
    //Cette variable est utilisée pour faire référence à un champ de texte dans l'interface utilisateur de l'application, qui est utilisé pour accepter une entrée
    @FXML
    private Label usermdp;
    //Cette variable est utilisée pour faire référence à un champ de texte dans l'interface utilisateur de l'application, qui est utilisé pour accepter une entrée
    @FXML
    private Label usernom;
    //Cette variable est utilisée pour faire référence à un champ de texte dans l'interface utilisateur de l'application, qui est utilisé pour accepter une entrée
    @FXML
    private Label useroption;
    //Cette variable est utilisée pour faire référence à un champ de texte dans l'interface utilisateur de l'application, qui est utilisé pour accepter une entrée
    @FXML
    private Label userprenom;
    //La variable "connection" est de type "Connection" et est initialisée à "null", ce qui signifie qu'elle ne contient aucune valeur pour l'instant. Cette variable sera utilisée pour stocker une connexion à une base de données.                         
    Connection connection = null;

    //La variable "preparedStatement" est de type "PreparedStatement" et est également initialisée à "null". Cette variable sera utilisée pour stocker une instruction SQL préparée, qui peut être utilisée pour exécuter des requêtes à une base de données.
    PreparedStatement preparedStatement = null;

    // La variable "Statement" est de type "Statement" et est également initialisée à "null". Cette variable sera utilisée pour stocker une instruction SQL générale qui peut être utilisée pour exécuter des requêtes à une base de données.
    Statement Statement = null;

    //La variable "resultSet" est de type "ResultSet" et est également initialisée à "null". Cette variable sera utilisée pour stocker le résultat d'une requête à une base de données.
    ResultSet resultSet = null;

    // La variable "dialogStage" est de type "Stage" et est initialisée avec une nouvelle instance de "Stage". Cette variable est utilisée pour stocker une fenêtre de dialogue.
    Stage dialogStage = new Stage();

    //La variable "scene" est de type "Scene" et n'est pas initialisée. Elle sera utilisée pour stocker une scène ( une vue, un contenu graphique) pour une application JavaFX.
    Scene scene;

    //La variable "sql" est de type "String" et est initialisée avec une instruction SQL qui sélectionne toutes les colonnes de la table "etudiant" où l'id de l'étudiant est égal à la variable "loggedInUserId". Cette instruction SQL sélectionnera les informations d'un utilisateur connecté à partir de la base de données en fonction de son ID.
    String sql = "SELECT * FROM etudiant WHERE id_etudiant = \'"+loggedInUserId+"';";

    //La variable "updateprofile" est de type "String" et est initialisée avec une instruction SQL qui met à jour les colonnes "nom", "prenom", "niveau" et "mot de passe" de la table "etudiant" où l'id de l'étudiant est égal à la variable "loggedInUserId". Les valeurs à mettre à jour sont définies par des "?" qui seront remplacées par des valeurs concrètes lors de l'exécution de la requête. Cette instruction SQL permet de mettre à jour les informations d'un utilisateur connecté dans la base de données.
    String updateprofile = "UPDATE etudiant SET nom = ?, prenom = ? , niveau = ?, password = ? WHERE id_etudiant = \'"+loggedInUserId+"';";

    //Ce code est une méthode de la classe actuelle qui surcharge (override) la méthode "initialize" de la classe parente.
    //La méthode "initialize" est une méthode de la classe "Initializable" qui est implémentée par la classe dans laquelle cette méthode est définie. Elle prend en entrée deux arguments de type URL et ResourceBundle qui sont utilisés pour définir les informations de localisation de la vue associée.
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
            //Dans cette implémentation, la méthode "initialize" appelle une autre méthode "afficherValeurs()" qui est utilisée pour afficher les informations de l'utilisateur connecté .
        afficherValeurs();
    }
   

    //Un constructeur est une méthode spéciale qui est appelée lorsqu'une nouvelle instance d'une classe est créée. Il est utilisé pour initialiser les propriétés ou les états de l'objet.
    //Dans ce cas précis, lorsqu'une nouvelle instance de la classe ProfileController est créée, le constructeur appelle la méthode "connectdb()" de la classe "ConnectionUtil" pour établir une connexion à une base de données, et stocke cette connexion dans la variable "connection". Cette connexion pourra être utilisée pour exécuter des requêtes à la base de données dans d'autres méthodes de la classe ProfileController.
    public ProfileController() {
        // La variable "connection" est de type "Connection" qui est une interface JDBC qui permet de connecter à une base de données.
        // La méthode "connectdb()" est supposée se charger de la configuration nécessaire pour établir une connexion avec une base de données, comme les informations d'identification pour se connecter à un serveur de base de données.
        // Une fois la connexion établie, elle pourra être utilisée pour exécuter des requêtes (select, insert, update, delete) à la base de données.
        connection = ConnectionUtil.connectdb();
    }


    // Cette méthode "afficherValeurs()" est utilisée pour afficher les informations d'un utilisateur connecté
    private void afficherValeurs() {
        try {
            //La méthode commence par créer un objet "Statement" en utilisant la méthode "createStatement()" de l'objet "connection" qui est de type "Connection" et qui contient une connexion établie à une base de données.
            Statement = connection.createStatement();
            //Ensuite, elle exécute une requête SQL stockée dans la variable "sql" qui sélectionne toutes les colonnes de la table "etudiant" où l'id de l'étudiant est égal à la variable "loggedInUserId" en utilisant la méthode "executeQuery()" de l'objet "Statement" qui retourne un objet de type "ResultSet" qui contient les résultats de la requête.
            resultSet = Statement.executeQuery(sql);
            //Ensuite, elle vérifie si il y a des résultats en utilisant la méthode "next()" de l'objet "ResultSet" qui retourne "true" si il y a des résultats et "false" sinon.
            if(resultSet.next()){
                //Si il y a des résultats, elle récupère les valeurs des colonnes "nom", "prenom", "email", "niveau" et "mot de passe" en utilisant les méthodes "getString()" de l'objet "ResultSet" en passant en paramètre le nom de la colonne.
                String nom = resultSet.getString("nom").toUpperCase();
                String prenom = resultSet.getString("prenom").toUpperCase();
                String Username = nom +" "+ prenom ;

                //Ensuite, elle met à jour les champs de saisie de la vue avec les valeurs récupérées en utilisant les méthodes "setText()" des objets de type "TextField" qui correspondent aux champs de saisie de la vue.
                textUsername.setText(Username);
                useremail.setText(resultSet.getString("email").toUpperCase());
                textUsername.setText(Username);
                txtnom.setText(nom);
                txtprenom.setText(prenom);
                txtniveau.setText(resultSet.getString("niveau"));
                useremail.setText(resultSet.getString("email"));
                txtpassword.setText(resultSet.getString("password"));
            }
        } catch (SQLException e) {
            //Si une exception est levée, elle est capturée et gérée à l'aide de la méthode "printStackTrace()" de l'objet "SQLException" qui permet d'afficher les détails de l'exception dans la console.
            e.printStackTrace();
        }
    };


    //Cette méthode "updateData(ActionEvent event)"  est utilisé pour mettre à jour les informations d'un utilisateur connecté dans la base de données. Il est lié à un bouton dans la vue qui permet à l'utilisateur de sauvegarder les modifications qu'il a apportées aux informations.
    @FXML
    void updateData(ActionEvent event) {
            try {
            //La méthode commence par créer un objet "PreparedStatement" en utilisant la méthode "prepareStatement()" de l'objet "connection" qui est de type "Connection" et qui contient une connexion établie à une base de données. La variable "updateprofile" contient l'instruction SQL pour mettre à jour les informations dans la table "etudiant"
            preparedStatement = connection.prepareStatement(updateprofile);
            //Ensuite, elle utilise les méthodes "setString()" de l'objet "preparedStatement" pour remplacer les "?" dans la requête SQL par les valeurs saisies dans les champs de saisie de la vue. Les méthodes "getText()" des objets "TextField" sont utilisées pour récupérer les valeurs saisies.
            preparedStatement.setString(1, txtnom.getText().toString());
            preparedStatement.setString(2, txtprenom.getText().toString());
            preparedStatement.setString(3, txtniveau.getText().toString());
            preparedStatement.setString(4, txtpassword.getText().toString());
            
            //Ensuite, elle exécute la requête SQL en utilisant la méthode "executeUpdate()" de l'objet "preparedStatement" qui retourne un entier qui indique le nombre de lignes affectées par la requête.
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            //Si une exception est levée, elle est capturée et gérée à l'aide de la méthode "printStackTrace()" de l'objet "Exception" qui permet d'afficher les détails de l'exception dans la console.
            e.printStackTrace();
        }
        //Enfin, la méthode appelle la méthode "afficherValeurs()" pour mettre à jour les informations affichées dans la vue.
        afficherValeurs();
    }
}
