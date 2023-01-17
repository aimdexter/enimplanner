CREATE TABLE etudiant (
	id_etudiant serial PRIMARY KEY,
	nom VARCHAR ( 50 ) NOT NULL,
	prenom VARCHAR ( 50 ) NOT NULL,
	niveau VARCHAR ( 50 ) NOT NULL,
	email VARCHAR ( 50 ) UNIQUE NOT NULL,
	password VARCHAR ( 50 ) NOT NULL
);




CREATE TABLE exam (
	id_exam serial PRIMARY KEY,
	nom_exam VARCHAR ( 50 ) UNIQUE NOT NULL,
	date_exam DATE NOT NULL,

	id_etudiant INT NOT NULL,
      FOREIGN KEY(id_etudiant) 
	  REFERENCES etudiant(id_etudiant),

	id_matiere INT NOT NULL,
      FOREIGN KEY(id_matiere) 
	  REFERENCES matiere(id_matiere)
);

CREATE TABLE matiere (
	id_matiere serial PRIMARY KEY,
	nom_matiere VARCHAR ( 50 ) UNIQUE NOT NULL,
	date_matiere DATE NOT NULL,
	id_etudiant INT NOT NULL,
      FOREIGN KEY(id_etudiant) 
	  REFERENCES etudiant(id_etudiant)
);

CREATE TABLE todo (
	id_todo serial PRIMARY KEY,
	nom_todo VARCHAR ( 50 ) UNIQUE NOT NULL,
	date_todo DATE NOT NULL,	
	id_etudiant INT NOT NULL,
      FOREIGN KEY(id_etudiant) 
	  REFERENCES etudiant(id_etudiant)
);