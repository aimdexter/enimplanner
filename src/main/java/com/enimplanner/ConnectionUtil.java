package com.enimplanner;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionUtil {
    Connection conn = null;

    // La méthode commence par enregistrer le pilote JDBC pour PostgreSQL en utilisant la méthode "forName" de la classe "Class". Cela permet à l'application de se connecter à la base de données en utilisant le protocole JDBC.
    // Ensuite, la méthode utilise la méthode "getConnection" de la classe "DriverManager" pour établir une connexion à la base de données en spécifiant les informations de connexion nécessaires:
    // "jdbc:postgresql://localhost:49153/studyenim" est l'URL de la base de données, qui spécifie le protocole (jdbc), le type de base de données (postgresql), l'adresse du serveur (localhost) et le numéro de port (49153) et le nom de la base de données (studyenim).
    // "postgres" est le nom d'utilisateur pour se connecter à la base de données.
    // "postgrespw" est le mot de passe associé à ce nom d'utilisateur.
    // Si la connexion est établie avec succès, la méthode renvoie l'objet Connection créé. Sinon, une exception est levée et un message d'erreur est affiché à l'aide de la méthode "println" de l'objet "System.err".
    
    // La méthode retourne finalement null si une exception est levée.    
    public static Connection connectdb() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:49153/studyenim", "postgres","postgrespw");
            return conn;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }
}
