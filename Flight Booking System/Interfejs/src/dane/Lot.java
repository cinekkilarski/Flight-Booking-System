package dane;

import java.io.Serializable;

public class Lot implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2557663445777362700L;
	private String wylot, przylot, data;
	private int cena;

	public Lot(String wylot, String przylot, int cena, String data) {
		this.wylot = wylot;
		this.przylot = przylot;
		this.cena = cena;
		this.data = data;

	}

	public String getWylot() {
		return wylot;
	}

	public String getPrzylot() {
		return przylot;
	}

	public int getCena() {
		return cena;
	}

	public String getData() {
		return data;
	}

}
