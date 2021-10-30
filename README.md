# Caldav PC3R
![enter image description here](https://raw.githubusercontent.com/isabelleshao/Caldav-PC3R/742fa6360a25478bed4ddf349bf49bfd78885ba7/imgs/01.png)

## Vue d'ensemble


PC3R Caldav est un site permettant aux étudiants de Master Informatique de consulter  leur emploi du temps, par le biais de l’API Caldav de Sorbonne université. Elle permet en outre d’annoter des cours et de lancer un pomodoro.

## Technologies utilisés

1. Java
2. Catalina 9
3. Gson
4. MariaDB
5. jQuery
6. Bootstrap
7. EvoCalendar
8. API Caldav
9. API openweathermap

## Description du projet

Caldav PC3R est un site multipage utilisant javascript, jQuery et Bootstrap pour la partie
front. Il fait appel à une base de données MariaDB, et utilise des servlets pour la partie
back.
L’idée est de pouvoir rassembler dans une interface plus user friendly et d’une latence moindre les ressources dont à besoin un élève de Sorbonne Université pour sa scolarité. Il y a bien évidemment le calendrier de son master, mais il permet également d’associer des commentaires et des pomodoros à ses cours, via le calendrier..

## Description de l’API

Nous utilisons l’API Caldav du master (https://cal.ufr-info-p6.jussieu.fr/master/) pour récupérer l’emploi du temps des élèves de master.
Chaque cursus génère un calendrier au format icalc accessible avec des méthodes HTTP, que nous parsons afin de le rendre exploitable sur notre site.
Un appel au calendrier de l’API se fait à chaque connexion de l’utilisateur, ainsi qu’à chaque changement de cursus dans le profil (ex: passer de M1_STL à M1_BIM).
## Annexe 

1) Tester le projet
Il est nécessaire de lancer catalina9 et MariaDB, et de peupler avec ces données:
Il faut créer un utilisateur MariaDB avec comme id:
 - user1
 -  password1
 
```
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
        dateAjout DATETIME NOT NULL,
        idCours varchar(128) NOT NULL,4
        note varchar(1024) NOT NULL,
        CONSTRAINT `fk_note_user`
        FOREIGN KEY (idUser) REFERENCES T_users (Login)
        ON DELETE CASCADE
        ) ENGINE=InnoDB;
        INSERT INTO T_users VALUES ('isa', 'isa','M1_STL');
        INSERT INTO T_pomodoro (idUser, d, duree, idCours) VALUES ('isa',
        ("2012-04-19 13:08:22") ,60, "desdsfsdfdsf");
```
## Visuels

![enter image description here](https://raw.githubusercontent.com/isabelleshao/Caldav-PC3R/main/imgs/02.png)
![enter image description here](https://raw.githubusercontent.com/isabelleshao/Caldav-PC3R/main/imgs/03.png)
![enter image description here](https://raw.githubusercontent.com/isabelleshao/Caldav-PC3R/main/imgs/04.png)
![enter image description here](https://raw.githubusercontent.com/isabelleshao/Caldav-PC3R/742fa6360a25478bed4ddf349bf49bfd78885ba7/imgs/01.png)
![enter image description here](https://raw.githubusercontent.com/isabelleshao/Caldav-PC3R/main/imgs/06.png)

![enter image description here](https://raw.githubusercontent.com/isabelleshao/Caldav-PC3R/main/imgs/07.png)
