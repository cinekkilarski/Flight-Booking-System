package dane;

import java.io.Serializable;

public class personal_data implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2557663445777362700L;
	private String imie;
	private String nazwisko;

	public personal_data(String imie, String nazwisko) {
		this.imie = imie;
		this.nazwisko = nazwisko;
	}

	public String getImie() {
		return imie;
	}

	public String getNazwisko() {
		return nazwisko;
	}

}
