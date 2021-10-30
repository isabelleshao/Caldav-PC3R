package objets;

public class Pomodoro {

	private String idPomodoro;
	private String idUser;
	private String date;
	private String duree;
	private String idCours;

	public Pomodoro(String idPomodoro, String user, String date, String duree, String idCours) {
		this.idPomodoro = idPomodoro;
		this.idUser = user;
		this.date = date;
		this.duree = duree;
		this.idCours = idCours;
	}

	public void addDuree(int mins) {
		this.duree += mins;
	}

	public String getUser() {
		return this.idUser;
	}

	public String toString() {
		return "Pomorodo[idPomodoro=" + idPomodoro + ", idUser=" + idUser + ", date=" + date + ", duree=" + duree
				+ ", idCours=" + idCours + "]";
	}

}
