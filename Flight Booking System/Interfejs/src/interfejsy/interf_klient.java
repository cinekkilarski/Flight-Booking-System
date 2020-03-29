package interfejsy;

import dane.Lot;
import dane.personal_data;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface interf_klient extends Remote {


	boolean szukaj_rezerwoj(String imie, String nazwisko, int hashkod) throws RemoteException;

	Map<Integer, Lot> wyszukaj_lot(String wylot, String przylot, int cena) throws RemoteException;

	List<Lot> przeslijLoty() throws RemoteException;

	List<personal_data> przeslijrezerwacje() throws RemoteException;

	boolean usun_rezerwacje(int hashkod) throws RemoteException;

	List<personal_data> przeslij_dokonana_rezerwacje() throws RemoteException;

	List<personal_data> przeslij_usunieta_rezerwacje() throws RemoteException;

	// List<Lot> lot_rez() throws RemoteException;
}
