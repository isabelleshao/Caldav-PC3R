package servlets;

import java.io.IOException;
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
import dao.DAOnote;
import objets.AuthReponse;
import objets.Note;

/**
 * Servlet implementation class ServletNote
 */
@WebServlet("/ServletNote")
public class ServletNote extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Gson gson;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletNote() {
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
		// TODO Auto-generated method stub
		
		String login = request.getParameter("txtLogin");
		if (login != null) {

			ArrayList<Note> notes = DAOnote.listerNote(login);

			response.setContentType("application/json");
	
			if (notes.size() > 0) {

				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(gson.toJson(notes));
			} else {
				response.getWriter().write(gson.toJson(null));

			}

		} else {
			System.out.println("Pas de login");
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String idCours = request.getParameter("idCours");
		String note = request.getParameter("note");
		String login = request.getParameter("txtLogin");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
		LocalDateTime l = LocalDateTime.now();

		boolean noteAjoutee = DAOnote.nouvelleNote(login, dtf.format(l), idCours, note);
		
		response.setContentType("application/json");
		

		if (noteAjoutee) {
			AuthReponse rep = new AuthReponse("ok", login); 
			response.getWriter().write(gson.toJson(rep));


		} else {
			response.getWriter().write(gson.toJson(new AuthReponse("ko", login)));

		}

	}

}
