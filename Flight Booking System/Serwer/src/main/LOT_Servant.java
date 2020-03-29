package main;

import dane.Lot;
import dane.personal_data;
import interfejsy.interf_klient;
import java.util.List;
import java.util.ArrayList;
//import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;

public class LOT_Servant extends UnicastRemoteObject implements interf_klient {

	private static final long serialVersionUID = -2557663445777362700L;
	List<personal_data> ostatnia_rezerwacja = new ArrayList<>();
	List<personal_data> usunieta_rezerwacja = new ArrayList<>();
	List<Lot> listaLotow = new ArrayList<>();
	// List<Lot> listaLotow1 = new ArrayList<>();
	List<Lot> listaLotowUsunietych = new ArrayList<>();
	List<Lot> listaLotowUsunietych2 = new ArrayList<>();
	List<personal_data> listaRezerwacji = new ArrayList<>();
	List<personal_data> bbb = new ArrayList<>();

	public LOT_Servant() throws RemoteException {
		listaLotow.add(new Lot("Krakow", "Warszawa", 480, "30.09.2019"));
		listaLotow.add(new Lot("Krakow", "Gdansk", 600, "22.10.2019"));
		listaLotow.add(new Lot("Krakow", "Wroclaw", 400, "06.10.2019"));
		listaLotow.add(new Lot("Warszawa", "Szczecin", 520, "18.09.2019"));
		listaLotow.add(new Lot("Warszawa", "Gdansk", 700, "20.11.2019"));
		listaLotow.add(new Lot("Warszawa", "Wroclaw", 350, "15.10.2019"));
		Collections.sort(listaLotow, Comparator.comparing(Lot::getWylot));
//		listaRezerwacji.add(new personal_data("Krystian", "Karczewski"));
//		listaRezerwacji.add(new personal_data("Jan", "Nowak"));
//		listaRezerwacji.add(new personal_data("Marcin", "Mazur"));
		Collections.sort(listaRezerwacji, Comparator.comparing(personal_data::getImie));
		Collections.sort(ostatnia_rezerwacja, Comparator.comparing(personal_data::getImie));
	}

	public synchronized boolean szukaj_rezerwoj(String imie, String nazwisko, int hashkod) {
		ostatnia_rezerwacja.removeAll(ostatnia_rezerwacja);
		listaRezerwacji.add(new personal_data(imie, nazwisko));
		listaLotowUsunietych2.removeAll(listaLotowUsunietych2);
		listaLotowUsunietych2 = listaLotow.stream().filter(a -> a.hashCode() == hashkod).collect(Collectors.toList());
		listaLotowUsunietych.add(listaLotowUsunietych2.get(0));
		listaLotow = listaLotow.stream().filter(a -> a.hashCode() != hashkod).collect(Collectors.toList());

		ostatnia_rezerwacja.add(new personal_data(imie, nazwisko));

		Collections.sort(listaLotow, Comparator.comparing(Lot::getWylot));

		return true;
	}

	public synchronized boolean usun_rezerwacje(int number) {
		// listaRezerwacji.removeAll(listaRezerwacji);
		usunieta_rezerwacja.removeAll(usunieta_rezerwacja);
		usunieta_rezerwacja.add(listaRezerwacji.get(number));
		listaRezerwacji.remove(number);

		// w przypadku anulowania rezerwacji, wybrany lot wraca na listê dostêpnych
		// polaczen
		listaLotow.add(listaLotowUsunietych.get(number));
		listaLotowUsunietych.remove(number);
		// Collections.sort(listaRezerwacji,
		// Comparator.comparing(personal_data::getImie));

		return true;
	}

	public List<Lot> przeslijLoty() {
		return listaLotow;
	}

	public List<personal_data> przeslijrezerwacje() {
		return listaRezerwacji;
	}

	public List<personal_data> przeslij_dokonana_rezerwacje() {
		return ostatnia_rezerwacja;
	}

	public List<personal_data> przeslij_usunieta_rezerwacje() {
		return usunieta_rezerwacja;
	}
//	public List<Lot> lot_rez() {
//		return listaLotowUsunietych;
//	}

	public synchronized Map<Integer, Lot> wyszukaj_lot(String wylot, String przylot, int cena) {
		List<Lot> szukanie = listaLotow;
		Map<Integer, Lot> Hashcode_Lot = new LinkedHashMap<>();
		if (!wylot.isEmpty())
			szukanie = szukanie.stream().filter(a -> a.getWylot().contains(wylot)).collect(Collectors.toList());
		if (!przylot.isEmpty())
			szukanie = szukanie.stream().filter(a -> a.getPrzylot().contains(przylot)).collect(Collectors.toList());
		if (cena != 0)
			szukanie = szukanie.stream().filter(a -> (a.getCena() >= cena - 100 && a.getCena() <= cena + 100))
					.collect(Collectors.toList());
		szukanie.forEach(a -> Hashcode_Lot.put(a.hashCode(), a));
		return Hashcode_Lot;
	}

}
