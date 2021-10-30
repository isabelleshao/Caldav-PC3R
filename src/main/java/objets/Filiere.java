package objets;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.tomcat.util.codec.binary.Base64;

public class Filiere {

	private String niveau; // ex : M1_STL
	private HashSet<String> matieres;
	private HashSet<HashMap<String, String>> calendrier;

	public Filiere(String niveau) {

		this.niveau = niveau;
		this.matieres = new HashSet<String>();
		this.setCalendrier();
	}

	public void setCalendrier() {
		String suffixe = this.niveau.split("_")[1] + "/" + this.niveau;
		this.calendrier = this.formatCalendrier(suffixe);

	}

	public String getNiveau() {
		return this.niveau;
	}

	public HashSet<HashMap<String, String>> getCalendrier() {
		return this.calendrier;
	}

	public static String auth(String suffixe) {

		String result = new String();
		try {
			// String webPage = "https://cal.ufr-info-p6.jussieu.fr/caldav.php/STL/M1_STL";
			String webPage = "https://cal.ufr-info-p6.jussieu.fr/caldav.php/" + suffixe;
			String name = "student.master";
			String password = "guest";

			String authString = name + ":" + password;
			System.out.println("auth string: " + authString);
			byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
			String authStringEnc = new String(authEncBytes);
			System.out.println("Base64 encoded auth string: " + authStringEnc);

			URL url = new URL(webPage);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			result = sb.toString();

			return result;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public HashSet<HashMap<String, String>> formatCalendrier(String suffixe) {

		HashSet<HashMap<String, String>> result = new HashSet<HashMap<String, String>>();

		System.out.println("start");

		String str = auth(suffixe);
		String[] tab = str.split("\n");

		int i = 0;

		// entre dans un nouvel evenement
		HashMap<String, String> evenement = new HashMap<>();
		String desc = new String();
		String badge = new String();
		LocalDate dateDebut = null;
		String idCours = new String();
		String name = new String();
		String horaire = new String();
		String nomComplet = new String();

		// si c'est un evenement recurent
		HashSet<LocalDate> dateExclus = new HashSet<>();
		ArrayList<LocalDate> dateRepetes = new ArrayList<>();
		LocalDate dateFin = null;

		int cpt = 0;

		while (i < tab.length) {

			String ligne = tab[i];
			ligne = ligne.substring(0, ligne.length() - 1);
			if (ligne.contains("BEGIN:VEVENT")) {
				// entre dans un nouvel evement, je remets toput à zero

				evenement = new HashMap<>();
				desc = new String();
				badge = new String();
				dateDebut = null;
				idCours = new String();
				name = new String();
				horaire = new String();
				nomComplet = new String();

				// si c'est un evenement recurent
				dateExclus = new HashSet<>();
				dateRepetes = new ArrayList<>();
				dateFin = null;

				cpt = 0;
			} else if (ligne.contains("DTSTART;TZID=")) {

				// V = timezone-id, HH instead of hh for 24-hour-clock, u for proleptic ISO-year
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("'DTSTART;TZID='VV:uuuuMMdd'T'HHmmss");
				// ligne = ligne.substring(0, ligne.length() - 1);
				ZonedDateTime zdt = ZonedDateTime.parse(ligne, dtf);
				Instant instant = zdt.toInstant();
				dateDebut = LocalDate.ofInstant(instant, ZoneId.systemDefault());
				LocalDateTime t = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
				String start = t.toString().substring(t.toString().length() - 5);
				horaire = start + horaire;

			} else if (ligne.contains("DTSTART;VALUE=DATE:")) {

				String date = ligne.replaceAll("DTSTART;VALUE=DATE:", "");
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuuMMdd");

				dateDebut = LocalDate.from(dtf.parse(date));

				horaire = "Toute la journée";

			} else if (ligne.contains("DTEND;TZID=")) {

				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("'DTEND;TZID='VV:uuuuMMdd'T'HHmmss");

				ZonedDateTime zdt = ZonedDateTime.parse(ligne, dtf);
				Instant instant = zdt.toInstant();
				LocalDate d = LocalDate.ofInstant(instant, ZoneId.systemDefault());
				LocalDateTime t = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
				String end = t.toString().substring(t.toString().length() - 5);

				horaire = " - " + end + "";

			} else if (ligne.contains("LOCATION:")) {

				desc += ligne.replaceAll("LOCATION:", "") + "";

			} else if (ligne.contains("SUMMARY:")) {
				String[] nomTemp = ligne.replaceAll("SUMMARY:", "").split("-");
				nomComplet = ligne.replaceAll("SUMMARY:", "");
				if (nomTemp.length > 1) {
					badge = nomTemp[0];
					name = ligne.replaceAll("SUMMARY:", "").replace(badge + "-", "");

					if (badge.startsWith("MU")) {
						this.matieres.add(badge + " - " + nomTemp[1]);
					}

				} else {
					name = nomTemp[0];
				}

			} else if (ligne.contains("UID:")) {
				idCours = ligne.replaceAll("UID:", "");
			} else if (ligne.contains("RRULE:FREQ=WEEKLY;") || ligne.contains("EXDATE;")) {

				if (ligne.contains("RRULE:FREQ=WEEKLY;")) {

					String[] parsing = ligne.split(";");

					String date = parsing[2].replaceAll("UNTIL=", "").replace("Z", "");
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuuMMdd'T'HHmmss");
					LocalDate d = LocalDate.from(dtf.parse(date));
					dateFin = d;
				} else {

					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("'EXDATE;TZID='VV:uuuuMMdd'T'HHmmss");
					ZonedDateTime zdt = ZonedDateTime.parse(ligne, dtf);
					Instant instant = zdt.toInstant();
					LocalDate d = LocalDate.ofInstant(instant, ZoneId.systemDefault());
					dateExclus.add(d);
				}

			} else if (ligne.contains("END:VEVENT")) {

				evenement.put("name", name);
				evenement.put("date", dateDebut.toString());
				evenement.put("badge", badge);
				evenement.put("description", nomComplet + "<br>" + desc + "<br>" + horaire);
				evenement.put("type", "event");
				evenement.put("id", idCours);
				result.add(evenement);

				if (dateFin != null) {

					// generer toutes les dates entre le DTSTART et RRULE:FREQ en excluant EXDATE

					LocalDate dateTemp = LocalDate.from(dateDebut).plusWeeks(1);
					while (dateTemp.isBefore(dateFin)) {
						if (!dateExclus.contains(dateTemp)) {

							evenement = new HashMap<>();

							evenement.put("name", name);
							evenement.put("date", dateTemp.toString());
							evenement.put("badge", badge);
							evenement.put("description", nomComplet + "<br>" + desc + "<br>" + horaire);
							evenement.put("type", "event");
							evenement.put("id", (idCours + "OCCURENCE" + cpt++));

							result.add(evenement);

							dateTemp = LocalDate.from(dateTemp).plusWeeks(1);
						} else {
							dateTemp = LocalDate.from(dateTemp).plusWeeks(1);
						}

					}

				}

			}

			i++;
		}

		return result;

	}

	public static void main(String[] args) {

		Filiere M1_STL = new Filiere("M1_STL");

		System.out.println(Arrays.asList(M1_STL.getCalendrier()));

	}

}
