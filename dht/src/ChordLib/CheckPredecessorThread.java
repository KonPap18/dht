package ChordLib;

import java.rmi.ConnectException;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import DistributedStorageApp.AppNodeInt;

public class CheckPredecessorThread implements Runnable {
	private final AppNodeInt node;
	private boolean nodeAnswered;

	public CheckPredecessorThread(AppNodeInt node) {
		this.node = node;
	}

	@Override
	public void run() {
		AppNodeInt tempPre = null;

		try {
			if (node.getPredecessor() == null) {
				System.out.println("no pred ");
				return;// no pred
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Registry rmi = null;
		try {
			rmi = LocateRegistry.getRegistry(node.getPredecessor().getIP());
		} catch (RemoteException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if (rmi != null) {
			System.out.println("check pred gained access to remote's registry");

			// αν δεν ειναι null
			try {
				tempPre = (AppNodeInt) rmi.lookup(String.valueOf(node
						.getPredecessor().getPID()));
			} catch (Exception e) {
				// e.printStackTrace();
				System.out
						.println("My pre is down and probably he didn't informed");
			}

			if (tempPre != null) {
				try {
					nodeAnswered = node.answer();
					System.out.println("my predeseccor "
							+ tempPre.getNodeKey().toString() + " is alive");
				} catch (ConnectException e) {
					System.out
							.println("my pred is dead or something he cant't reply");
					try {
						node.setPredecessor(null);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (!nodeAnswered) {
					// pre is dead
					try {
						node.setPredecessor(null);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		} else {
			System.out
					.println("check pred NULL remote registry shit is about to go ");
		}
		nodeAnswered = false;

	}

}
