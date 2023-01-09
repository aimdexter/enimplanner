package com.enimplanner;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionUtil {
    Connection conn = null;

    public static Connection connectdb() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/studyenim", "postgres",
                    "youssra2020");
            return conn;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }
}
