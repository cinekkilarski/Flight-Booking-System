package main;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LOT_Server {
	Registry reg; // rejestr nazw obiektow
	LOT_Servant servant; // klasa uslugowa

	public static void main(String[] args) {
		try {
			new LOT_Server();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	protected LOT_Server() throws RemoteException {
		try {
			reg = LocateRegistry.createRegistry(1099); // utworzenie rejestru nazw
			servant = new LOT_Servant(); // utworzenie zdalnego obiektu
			reg.rebind("LOT_Server", servant); // zwiazanie nazwy z obiektem
			System.out.println("LOT_Server Gotowy do uzycia!");
		} catch (RemoteException e) {
			e.printStackTrace();
			throw e;
		}
	}
}
