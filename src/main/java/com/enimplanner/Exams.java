package com.enimplanner;

import java.sql.Date;


//déclare une nouvelle classe en Java nommée "Exams". Cette classe peut être utilisée pour créer des objets qui représentent des examens, et peut contenir des méthodes et des propriétés qui sont liées aux examens.
public class Exams {
    int id_exam;
    String nom_exam;
    Date date_exam;
    int id_etudiant;

    public Exams() {
        super();
    }

    public Exams(int id_exam, String nom_exam, Date date_exam, int id_etudiant) {
        this.id_exam = id_exam;
        this.nom_exam = nom_exam;
        this.date_exam = date_exam;
        this.id_etudiant = id_etudiant;
    }
    public int getId_exam() {
        return id_exam;
    }
    public void setId_exam(int id_exam) {
        this.id_exam = id_exam;
    }
    public String getNom_exam() {
        return nom_exam;
    }
    public void setNom_exam(String nom_exam) {
        this.nom_exam = nom_exam;
    }
    public Date getDate_exam() {
        return date_exam;
    }
    public void setDate_exam(Date date_exam) {
        this.date_exam = date_exam;
    }
    public int getId_etudiant() {
        return id_etudiant;
    }
    public void setId_etudiant(int id_etudiant) {
        this.id_etudiant = id_etudiant;
    }
}
