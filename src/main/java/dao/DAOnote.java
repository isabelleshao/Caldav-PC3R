package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import objets.Note;
import objets.Pomodoro;

public class DAOnote extends DAOContext {
	public static boolean nouvelleNote(String u, String d, String idCours, String txt) {

		String strSql = "INSERT INTO T_note (idUser, dateAjout, idCours, note) VALUES (?,?,?,?)";

		try (Connection connection = DriverManager.getConnection(dbURL, dbLogin, dbPassword)) {

			try (PreparedStatement statement = connection.prepareStatement(strSql)) {

				statement.setString(1, u);
				statement.setString(2, d);
				statement.setString(3, idCours);
				statement.setString(4, txt);

				try (ResultSet resultSet = statement.executeQuery()) {

					System.out.println("note added");
					return true;
				}
			}
		} catch (Exception exception) {
			throw new RuntimeException(exception);

		}

	}

	public static ArrayList<Note> listerNote(String user) {

		String strSql = "select * from T_note where idUser=?";
		ArrayList<Note> resultat = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(dbURL, dbLogin, dbPassword)) {

			try (PreparedStatement statement = connection.prepareStatement(strSql)) {

				statement.setString(1, user);

				try (ResultSet rs = statement.executeQuery()) {

					while (rs.next()) {
						String idNote = rs.getString("idNote");
						String idUser = rs.getString("idUser");
						String dateAjout = rs.getString("dateAjout");
						String idCours = rs.getString("idCours");
						String note = rs.getString("note");

						Note n = new Note(idNote, idUser, dateAjout, note, idCours);
						resultat.add(n);
					}

					return resultat;
				}
			}
		} catch (Exception exception) {

			throw new RuntimeException(exception);

		}
	}

}
