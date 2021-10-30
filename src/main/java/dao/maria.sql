DROP DATABASE IF EXISTS Caldav;
CREATE DATABASE Caldav;
USE Caldav; 


CREATE TABLE T_users(
Login varchar(32) NOT NULL UNIQUE PRIMARY KEY,
Password varchar(32) NOT NULL,
Filiere varchar(32) NOT NULL
) ENGINE=InnoDB;


CREATE TABLE T_pomodoro(
idPomodoro int(4) PRIMARY KEY AUTO_INCREMENT,
idUser varchar(32),
d DATETIME NOT NULL,
duree int(4) NOT NULL, 
idCours varchar(32) NOT NULL,
 CONSTRAINT `fk_pom_user`
    FOREIGN KEY (idUser) REFERENCES T_users (Login)
    ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE T_note(
idNote int(4) PRIMARY KEY AUTO_INCREMENT,
idUser varchar(32)NOT NULL,
dateAjout  DATETIME NOT NULL,
idCours varchar(128) NOT NULL,
note varchar(1024) NOT NULL,
 CONSTRAINT `fk_note_user`
    FOREIGN KEY (idUser) REFERENCES T_users (Login)
    ON DELETE CASCADE
    
) ENGINE=InnoDB;

INSERT INTO T_users VALUES ('isa', 'isa','M1_STL');
INSERT INTO T_pomodoro (idUser, d, duree, idCours) VALUES ('isa', ("2012-04-19 13:08:22") ,60, "desdsfsdfdsf");

