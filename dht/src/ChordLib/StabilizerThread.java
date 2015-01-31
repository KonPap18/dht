package ChordLib;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;

import DistributedStorageApp.AppNodeInt;

public class StabilizerThread implements Runnable {
	private final AppNodeInt node;
	
	public StabilizerThread(AppNodeInt node) {
		this.node=node;
	}
	
	@Override
	public void run() {
		
		boolean rmiIsDown=false;
		// TODO Auto-generated method stub
		Registry registry=null;
		try {
			registry=LocateRegistry.createRegistry(1099);
			rmiIsDown=true;
		}catch(ExportException e) {
			//that means there is an rmi nregistry active
		} catch (RemoteException e) {
			//not sure probasply rmi is down also 
			e.printStackTrace();
		}
	}

}
