package servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dao.DAOContext;
import dao.DAOPomodoro;
import dao.DAOUser;
import objets.AuthReponse;
import objets.Pomodoro;
import objets.User;

@WebServlet("/ServletPomodoro")
public class ServletPomodoro extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Gson gson;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletPomodoro() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		DAOContext.init(this.getServletContext());
		this.gson = new GsonBuilder().serializeNulls().create();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String login = request.getParameter("txtLogin");
		if (login != null) {

			ArrayList<Pomodoro> pomodoro = DAOPomodoro.listerPomodoro(login);

			response.setContentType("application/json");
			if (pomodoro.size() > 0) {

				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(gson.toJson(pomodoro));
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
		int duree = Integer.parseInt(request.getParameter("txtDuree"));
		String cours = request.getParameter("txtIDcours");

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
		LocalDateTime l = LocalDateTime.now();

		boolean pomodoroCree = DAOPomodoro.nouveauPomodoro(login, dtf.format(l), duree, cours);
		response.setContentType("application/json");

		if (pomodoroCree) {
			AuthReponse rep = new AuthReponse("ok", login);
			response.getWriter().write(gson.toJson(rep));

		} else {
			response.getWriter().write(gson.toJson(new AuthReponse("ko", login)));
		}

	}

}
