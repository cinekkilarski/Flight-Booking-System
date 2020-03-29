package main;

import dane.Lot;
import dane.personal_data;
import interfejsy.interf_klient;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Klient {
	private Scanner userInput = new Scanner(System.in);

	interf_klient zdalnyObiekt; // referencja do zdalnego obiektu
	Registry reg; // rejestr nazw obiektow
	int it;
	public boolean status;

	public static void main(String[] args) {
		new Klient();

		System.exit(1);
	}

	public Klient() {
		try {
			reg = LocateRegistry.getRegistry("localhost");
			zdalnyObiekt = (interf_klient) reg.lookup("LOT_Server");
			loop();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	private void loop() {
		while (true) {
			String line;
			System.out.println(
					"Wybierz czynnosc: [l]ista dostepnych lotow | [s]zukaj i rezerwuj lot | [r]ezerwacje dokonane | [u]sun rezerwacje | [w]yjscie");
			if (userInput.hasNextLine()) {
				line = userInput.nextLine();
				if (!line.matches("[lsruw]")) {
					System.out.println("Nieznana komenda");
					continue;
				}
				switch (line) {
				case "l":
					wyswietl_dostepne_loty();
					break;
				case "s":
					szukaj_i_rezerwuj();
					break;
				case "r":
					wyswietl_dokonane_rezerwacje();
					break;
				case "u":
					usunRezerwacje();
					break;
				case "w":
					System.out.println("Dziêkujemy za skorzystanie z naszych uslug!");
					return;
				}
			}
		}
	}

	private boolean sprawdzLiczbe(String input) {
		boolean integer = true;
		try {
			Integer.parseInt(input);
		} catch (NumberFormatException e) {
			integer = false;
		}
		return integer;
	}

	private int wczytanaLiczba() {
		int liczba;
		while (true) {
			if (userInput.hasNextLine()) {
				String line = userInput.nextLine();
				if (sprawdzLiczbe(line)) {
					liczba = Integer.valueOf(line);
					break;
				} else {
					System.out.println("Postêpuj zgodnie z wytycznymi!");
				}
			}
		}
		return liczba;
	}

	private void wyswietl_dostepne_loty() {
		List<Lot> listaKsiazek = null;
		try {
			int nr_z_listy = 0;
			listaKsiazek = zdalnyObiekt.przeslijLoty();
			System.out.println("Np.| Wylot | Przylot | Cena | Data wylotu");
			for (Lot a : listaKsiazek) {
				System.out.println(++nr_z_listy + " :" + a.getWylot() + "    " + a.getPrzylot() + "  " + a.getCena()
						+ "  " + a.getData());
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

//	private void lot_rez() {
//		List<Lot> listaKsiazek = null;
//		try {
//			int nr_z_listy = 0;
//			listaKsiazek = zdalnyObiekt.lot_rez();
//			System.out.println("Np.| Wylot | Przylot | Cena | Data wylotu");
//			for (Lot a : listaKsiazek) {
//				System.out.println(++nr_z_listy + " :" + a.getWylot() + "    " + a.getPrzylot() + "  " + a.getCena()
//						+ "  " + a.getWolne());
//			}
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
//
//	}
	private void szukaj_i_rezerwuj() {
		String wylot = "";
		String przylot = "";
		String imie = "";
		String nazwisko = "";
		int cena = 0;
		int nr_z_listy = 0;
		int numer = 0;

		try {
			while (true) {
				System.out.println("Podaj miejsce wylotu (klawisz ENTER - pomin):");
				if (userInput.hasNextLine()) {
					wylot = userInput.nextLine();
				}
				System.out.println("Podaj miejsce docelowe (klawisz ENTER - pomin):");
				if (userInput.hasNextLine()) {
					przylot = userInput.nextLine();
				}
				System.out.println("Cena: (szukana cena bazuje na równaniu: (x-100)<x<(x+100) ) (0 - pomin):");
				cena = wczytanaLiczba();
				break;
			}
			Map<Integer, Lot> rezultat = null;
			rezultat = zdalnyObiekt.wyszukaj_lot(wylot, przylot, cena);

			if (rezultat.size() > 0) {
				Map<Integer, Integer> mapaNumerHashkod = new HashMap<>();
				System.out.println("Znalezione polaczenia lotnicze: ");
				System.out.println("Np.| Wylot | Przylot | Cena | Data wylotu");
				for (Integer kod : rezultat.keySet()) {
					nr_z_listy++;

					System.out.println(nr_z_listy + ": " + rezultat.get(kod).getWylot() + "  "
							+ rezultat.get(kod).getPrzylot() + "     " + rezultat.get(kod).getCena() + "          "
							+ rezultat.get(kod).getData());
					mapaNumerHashkod.put(nr_z_listy, kod);
				}

				System.out.println(
						"Wybranie odpowiedniego numeru lotu z listy spowoduje przejœcie do procedury rezerwacji (0 - wyjscie)");

				while (true) {
					if (userInput.hasNextLine()) {
						String line = userInput.nextLine();
						if (sprawdzLiczbe(line)) {
							numer = Integer.valueOf(line);
							if (numer == 0)
								break;
							else if (numer < nr_z_listy + 1 && numer > 0) {
								System.out.println("imie (klawisz ENTER - pomin):");
								if (userInput.hasNextLine()) {
									imie = userInput.nextLine();
								}
								System.out.println("nazwisko (klawisz ENTER - pomin):");
								if (userInput.hasNextLine()) {
									nazwisko = userInput.nextLine();
								}

								zdalnyObiekt.szukaj_rezerwoj(imie, nazwisko, mapaNumerHashkod.get(numer));
								List<personal_data> listaRezerwacji = null;
								try {
									listaRezerwacji = zdalnyObiekt.przeslij_dokonana_rezerwacje();
									for (personal_data a : listaRezerwacji) {
										System.out.println("Gratulacje!");
										System.out.println("Rezerwacja na imie: " + a.getImie() + " i nazwisko: "
												+ a.getNazwisko() + " przebieg³a pomyœlnie!");
										System.out.println("Zyczymy udanej podrozy!");
										break;
									}
								} catch (RemoteException e) {
									e.printStackTrace();
								}

								break;
							} else {
								System.out.println("Podana liczba poza zakresem");
							}
						} else {
							System.out.println("Musisz podac numer z listy.");
						}
					}
				}
			} else {
				System.out.println("Brak dostêpnych lotow");
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void wyswietl_dokonane_rezerwacje() {
		List<personal_data> listaRezerwacji = null;
		try {
			int nr_z_listy = 0;
			listaRezerwacji = zdalnyObiekt.przeslijrezerwacje();
			System.out.println("Np. | Imie | Nazwisko");
			for (personal_data b : listaRezerwacji) {
				System.out.println(++nr_z_listy + " :      " + b.getImie() + "     " + b.getNazwisko());
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void usunRezerwacje() {
		List<personal_data> lista_rezerwacji_nr = null;
		int nr_z_listy = 0;
		int numer = 0;

		try {

			lista_rezerwacji_nr = zdalnyObiekt.przeslijrezerwacje();
			for (personal_data b : lista_rezerwacji_nr) {
				System.out.println(++nr_z_listy + " :" + b.getImie() + "      " + b.getNazwisko());
			}
			// System.out.println(nr_z_listy);
			System.out.println(
					"Wybranie odpowiedniego numeru lotu z listy spowoduje usuniêcie rezerwacji na wybrany lot (0 - wyjscie)");
			// lot_rez();
			while (true) {
				if (userInput.hasNextLine()) {
					String line = userInput.nextLine();
					if (sprawdzLiczbe(line)) {
						numer = Integer.valueOf(line);
						// System.out.println(numer);
						if (numer == 0)
							break;
						else if (numer < nr_z_listy + 1 && numer > 0) {
							zdalnyObiekt.usun_rezerwacje(numer - 1);
							List<personal_data> listaRezerwacji = null;
							try {
								listaRezerwacji = zdalnyObiekt.przeslij_usunieta_rezerwacje();

								for (personal_data a : listaRezerwacji) {
									System.out.println("Rezerwacja na imie: " + a.getImie() + " i nazwisko: "
											+ a.getNazwisko() + " zostala anulowana!");
									wyswietl_dokonane_rezerwacje();

									break;
								}
							} catch (RemoteException e) {
								e.printStackTrace();
							}
							break;
						} else {
							System.out.println("Podana liczba poza zakresem (0 - wyjscie).");
						}
					} else {
						System.out.println("Musisz podac numer z listy (0 - wyjscie).");
					}
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
