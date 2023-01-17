package com.enimplanner;

import java.util.Date;

public class Todos {
    public Todos(int id_todo, String nom_todo, Date date_todo, int id_etudiant) {
        this.id_todo = id_todo;
        this.nom_todo = nom_todo;
        this.date_todo = date_todo;
        this.id_etudiant = id_etudiant;
    }

    int id_todo;
    String nom_todo;
    Date date_todo;
    int id_etudiant;

    public Todos() {
        super();
    }

    public int getId_todo() {
        return id_todo;
    }

    public void setId_todo(int id_todo) {
        this.id_todo = id_todo;
    }

    public String getNom_todo() {
        return nom_todo;
    }

    public void setNom_todo(String nom_todo) {
        this.nom_todo = nom_todo;
    }

    public Date getDate_todo() {
        return date_todo;
    }

    public void setDate_todo(Date date_todo) {
        this.date_todo = date_todo;
    }

    public int getId_etudiant() {
        return id_etudiant;
    }

    public void setId_etudiant(int id_etudiant) {
        this.id_etudiant = id_etudiant;
    }
}
