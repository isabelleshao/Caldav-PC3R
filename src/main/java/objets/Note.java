package objets;

public class Note {

	private String idNote;
	private String idUser;
	private String date;
	private String idCours;
	private String note;

	public Note(String idNote, String user, String date, String note, String idCours) {
		this.idNote = idNote;
		this.idUser = user;
		this.date = date;
		this.note = note;
		this.idCours = idCours;
	}

	public String getUser() {
		return this.idUser;
	}

	public String toString() {
		return "Note[idNote=" + idNote + ", idUser=" + idUser + ", date=" + date + ", note=" + note
				+ ", idCours=" + idCours + "]";
	}

}
