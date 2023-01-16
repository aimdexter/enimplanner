package com.enimplanner;

import java.sql.Date;

public class Matieres {
    int id_matiere;
    String nom_matiere;
    Date date_matiere;
    int id_etudiant;

    public Matieres() {
        super();
    }

    public Matieres(int id_matiere, String nom_matiere, Date date_matiere, int id_etudiant) {
        this.id_matiere = id_matiere;
        this.nom_matiere = nom_matiere;
        this.date_matiere = date_matiere;
        this.id_etudiant = id_etudiant;
    }

    public int getId_matiere() {
        return id_matiere;
    }
    public void setId_matiere(int id_matiere) {
        this.id_matiere = id_matiere;
    }
    public String getNom_matiere() {
        return nom_matiere;
    }
    public void setNom_matiere(String nom_matiere) {
        this.nom_matiere = nom_matiere;
    }
    public Date getDate_matiere() {
        return date_matiere;
    }
    public void setDate_matiere(Date date_matiere) {
        this.date_matiere = date_matiere;
    }
    public int getId_etudiant() {
        return id_etudiant;
    }
    public void setId_etudiant(int id_etudiant) {
        this.id_etudiant = id_etudiant;
    }
}
