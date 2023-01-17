package com.enimplanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
// Ceci est une classe Java qui étend la classe "Application" de JavaFX, qui est utilisée pour créer des applications graphiques en Java.
// La classe définit une méthode "start" qui est appelée lorsque l'application démarre. Cette méthode prend en paramètre un objet "Stage", qui représente la fenêtre principale de l'application.
// La méthode commence par charger un fichier FXML, qui est un fichier de description de l'interface utilisateur. Cela est fait en utilisant la classe "FXMLLoader" et en spécifiant le chemin du fichier FXML à charger en utilisant la méthode "getResource" de la classe "getClass". Le résultat est un objet "Parent" qui est utilisé pour créer la scène de l'application.
// Ensuite, la méthode crée une nouvelle scène en utilisant l'objet "Parent" chargé et définit cette scène sur l'objet "Stage" en utilisant la méthode "setScene". Enfin, la méthode "show" de l'objet "Stage" est appelée pour afficher la fenêtre de l'application.
// La méthode "main" est utilisée pour lancer l'application. Il utilise la méthode statique "launch" pour démarrer l'application.
// La méthode "setRoot" n'est pas utilisé dans le code.
public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void setRoot(String string) {
    }

}