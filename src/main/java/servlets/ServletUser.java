package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dao.DAOContext;
import dao.DAOUser;
import objets.AuthReponse;
import objets.User;


/**
 * Servlet implementation class Login
 */
@WebServlet(urlPatterns = "/login", loadOnStartup = 1)
public class ServletUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Gson gson;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		DAOContext.init(this.getServletContext());
		this.gson = new GsonBuilder().serializeNulls().create();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * request.setAttribute("login", ""); request.setAttribute("password", "");
		 * request.setAttribute("errorMessage", "");
		 * request.getRequestDispatcher("/login.jsp").forward(request, response);
		 */
		String login = request.getParameter("txtLogin");
		String mdp = request.getParameter("txtPassword");
		if (login != null && mdp != null) {

			User connectedUser = DAOUser.isValidLogin(login, mdp);
			// Optional<Utilisateur> ores = base.trouveUtilisateur(login);
			response.setContentType("application/json");
			System.out.println("recuperation profil user " + login + " mdp: " + mdp);
			if (connectedUser != null) {

				System.out.println("recuperation profil Réussie.");
				System.out.println(connectedUser.toString());

				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(gson.toJson(connectedUser));
			} else {
				response.getWriter().write(gson.toJson(null));
				System.out.println("Connexion échoué");
			}

		} else {
			System.out.println("Pas de login");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String login = request.getParameter("txtLogin");
		String mdp = request.getParameter("txtPassword");
		String action = request.getParameter("txtAction");
		System.out.println("test" + action);
		System.out.println("test" + login);
		System.out.println("test" + mdp);

		if (login != null && mdp != null) {

			if (action.equals("login")) {

				User connectedUser = DAOUser.isValidLogin(login, mdp);

				// Optional<Utilisateur> ores = base.trouveUtilisateur(login);
				response.setContentType("application/json");
				System.out.println("Authentification par login: " + login + " mdp: " + mdp);
				if (connectedUser != null) {

					response.getWriter().write(gson.toJson(new AuthReponse("ok", login)));
					System.out.println("Connexion Réussie.");
					System.out.println(connectedUser.toString());
				} else {
					response.getWriter().write(gson.toJson(new AuthReponse("ko", login)));
					System.out.println("Connexion échoué");
				}
			} else if (action.equals("update")) {
				System.out.println("update");

				String newPass = request.getParameter("txtNewPass");
				String filiere = request.getParameter("txtFiliere");

				User connectedUser = DAOUser.updateUser(login, mdp, newPass, filiere);
				response.setContentType("application/json");
				System.out.println("Authentification par login: " + login + " mdp: " + mdp);

				if (connectedUser != null) {

					response.getWriter().write(gson.toJson(new AuthReponse("ok", login)));
					System.out.println("modification Réussie.");

				} else {
					response.getWriter().write(gson.toJson(new AuthReponse("ko", login)));
					System.out.println("Connexion échoué");
				}

			} else if (action.equals("signin")) {

				String filiere = request.getParameter("txtfiliere");
				System.out.println("Inscription reception post: " + login + " mdp: " + mdp+ " filiere: " + filiere);
				

				boolean inscriptionfaite = DAOUser.inscriptionUser(login, mdp, filiere);
				response.setContentType("application/json");
	

				if (inscriptionfaite) {

					response.getWriter().write(gson.toJson(new AuthReponse("ok", login)));
					System.out.println("Inscription Réussie.");

				} else {
					response.getWriter().write(gson.toJson(new AuthReponse("ko", login)));
					System.out.println("Inscription échoué");
				}

			}

		} else {
			response.getWriter().write(gson.toJson(new AuthReponse("ko", login )));
			System.out.println("Champ non rempli");
		}

	}
}
