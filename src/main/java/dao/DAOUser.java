package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import objets.User;

public class DAOUser extends DAOContext {

	public static User isValidLogin(String login, String password) {
		try (Connection connection = DriverManager.getConnection(dbURL, dbLogin, dbPassword)) {
			String strSql = "SELECT * FROM T_users WHERE login=? AND password=?";
			try (PreparedStatement statement = connection.prepareStatement(strSql)) {
				statement.setString(1, login);
				statement.setString(2, password);
				try (ResultSet resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						String lg = resultSet.getString("login");
						String pw = resultSet.getString("password");
						String fl = resultSet.getString("filiere");
						return new User(lg, pw, fl);

					} else {
						return null;
					}
				}
			}
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	public static User updateUser(String login, String password, String newPassword, String filiere) {
		System.out.println("connexion db avec pour updater le profil");

		try (Connection connection = DriverManager.getConnection(dbURL, dbLogin, dbPassword)) {

			String strSql = null;

			if (newPassword.equals("null")) { // pas de changement de mdp
				strSql = "UPDATE T_users SET Filiere=? WHERE login=?  AND password=?";
			} else {
				strSql = "UPDATE T_users SET Filiere=?, password=?  WHERE login=?  AND password=?";
			}

			try (PreparedStatement statement = connection.prepareStatement(strSql)) {
				statement.setString(1, filiere);

				if (newPassword.equals("null")) {
					statement.setString(2, login);
					statement.setString(3, password);

				} else {
					statement.setString(2, newPassword);
					statement.setString(3, login);
					statement.setString(4, password);

				}

				statement.executeUpdate();
				System.out.println("updater le profil : OK");
				if (newPassword.equals("null")) {
					return isValidLogin(login, password);
				} else {
					return isValidLogin(login, newPassword);
				}

			}

		} catch (Exception exception) {
			System.out.println("updater le profil : KO");
			throw new RuntimeException(exception);

		}

	}

	public static boolean inscriptionUser(String login, String password, String filiere) {

		String strSql = "INSERT INTO T_users VALUES (?,?,?)";

		try (Connection connection = DriverManager.getConnection(dbURL, dbLogin, dbPassword)) {

			try (PreparedStatement statement = connection.prepareStatement(strSql)) {

				statement.setString(1, login);
				statement.setString(2, password);
				statement.setString(3, filiere);

				try (ResultSet resultSet = statement.executeQuery()) {

					System.out.println("inscrption faite ");
					return true;

				}
			}
		} catch (Exception exception) {
			System.out.println("exeptionnnnnnnn ");
			throw new RuntimeException(exception);

		}
	}

}