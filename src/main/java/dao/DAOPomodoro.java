package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import objets.Pomodoro;

public class DAOPomodoro extends DAOContext {
	public static boolean nouveauPomodoro(String user, String d, int duree, String idCours) {

		String strSql = "INSERT INTO T_pomodoro (idUser, d, duree, idCours) VALUES (?,?,?,?)";

		try (Connection connection = DriverManager.getConnection(dbURL, dbLogin, dbPassword)) {

			try (PreparedStatement statement = connection.prepareStatement(strSql)) {

				statement.setString(1, user);
				statement.setString(2, d);
				statement.setInt(3, duree);
				statement.setString(4, idCours);

				try (ResultSet resultSet = statement.executeQuery()) {

					return true;

				}
			}
		} catch (Exception exception) {
			throw new RuntimeException(exception);

		}
	}

	public static ArrayList<Pomodoro> listerPomodoro(String user) {

	String strSql = "select * from T_pomodoro where idUser=?";
		ArrayList<Pomodoro> resultat = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(dbURL, dbLogin, dbPassword)) {

			try (PreparedStatement statement = connection.prepareStatement(strSql)) {

				statement.setString(1, user);

				try (ResultSet rs = statement.executeQuery()) {


					while (rs.next()) {
						String idPomodoro = rs.getString("idPomodoro");
						String idUser = rs.getString("idUser");
						String date = rs.getString("d");
						String duree = rs.getString("duree");
						String idCours = rs.getString("idCours");

						Pomodoro p = new Pomodoro(idPomodoro, idUser, date, duree, idCours);
						resultat.add(p);
					}

					return resultat;
				}
			}
		} catch (Exception exception) {

			throw new RuntimeException(exception);

		}
	}

}
