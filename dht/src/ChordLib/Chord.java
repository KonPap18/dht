package ChordLib;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import DistributedStorageApp.AppNode;
import DistributedStorageApp.AppNodeInt;
import Hash.Key;
import Hash.NodeKey;

public class Chord implements Remote {
	/*
	 * new chord ring starts with with the specified node
	 */
	public static void createRing(AppNode appNode) {
		System.out.println("Chord trying to start node " + appNode.getNodeKey()
				+ "\n");

		try {
			appNode.setConnected(true);
			// appNode.setSuccessor(appNode.getNodeKey());
			appNode.addSuccessor(appNode.getNodeKey(), 0);
			// appNode.addFinger(0, appNode.getNodeKey());//was
			// appNode.getSuccessor()
			// o eautos tou
			appNode.setPredecessor(null);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * checkarei an exoume pre kai i an o pre pou dinoume einai pio konta
	 */
	public static void notify(AppNodeInt thisNode, AppNodeInt n)
			throws RemoteException {
		// check an o pre einai null, kai an o pi8anos pre einai anamesa sto
		// kleidi tou kommvou kai ston twrino pre
		// an isxuei allazoume ton pre
		if (thisNode.getPredecessor() == null
				|| Key.isBetweenNotify(n.getNodeKey(),
						thisNode.getPredecessor(), thisNode.getNodeKey())) {
			thisNode.setPredecessor(n.getNodeKey());
			System.out.println("Predecessor changed for node "
					+ thisNode.getNodeKey().getPID() + "new predecessor "
					+ n.getNodeKey().getPID());
		}
	}

	public static void join(AppNodeInt thisNode, AppNodeInt entryPointNode)
			throws RemoteException {
		System.out.println("Chord trying to join node "
				+ thisNode.getNodeKey().getPID() + "withn entry point node "
				+ entryPointNode.getNodeKey().getPID());
		thisNode.setPredecessor(null);

		NodeKey successorKey = null;

		successorKey = entryPointNode.find_successor_NodeID(thisNode
				.getNodeKey());
		if (successorKey == null) {
			System.out
					.println("find successor is fucked up, returns null and shit");
		}
		// thisNode.setSuccessor(successorKey);
		thisNode.addSuccessor(successorKey, 0);
		if (thisNode.getFingersSize() > 1) {
			thisNode.clearFingers();
		}
		thisNode.setFinger(0, successorKey);
		// zhtame ton successor
		try {
			// System.out.println("joining node is looking up for "+"rmi:/" +
			// entryPointNode.getNodeKey().getIP() + ":1099/" +
			// String.valueOf(successorKey.getPID()));
			System.out.println("join in chord is lookin up for " + "/"
					+ entryPointNode.getNodeKey().getIP() + ":1099/"
					+ entryPointNode.getSignature().getRmiRecord());
			// AppNodeInt n = (AppNodeInt) Naming.lookup("rmi:/" +
			// entryPointNode.getNodeKey().getIP() + ":1099/" +
			// String.valueOf(successorKey.getPID()));
			// AppNodeInt
			// n=(AppNodeInt)Naming.lookup("/"+entryPointNode.getNodeKey().getIP()+":1099/"+entryPointNode.getSignature().getRmiRecord());
			AppNodeInt n = (AppNodeInt) Naming.lookup(entryPointNode
					.getSignature().getRmiRecord());

			// n ειναι ο successor toy thisnode
			Chord.notify(thisNode, n);// ελεγχος αν ο επόμενος
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Finally this node i now joined with succesor "
				+ thisNode.getSuccessor().getPID() + " and predeseccor "
				+ thisNode.getPredecessor().getPID());

	}

	public static AppNodeInt find_successor(AppNodeInt askingNode,
			Key idToSearch) throws MalformedURLException, NotBoundException,
			RemoteException {
		System.out.println("Entered method find successor in chord.Caller is "
				+ askingNode.getNodeKey().getPID() + "\n");
		AppNodeInt tmpAppNode = find_predecessor(askingNode, idToSearch);
		// exoume ton proigoumeno.o epomenos tou proigoumenou einai o successor
		// pou psaxnoume peidh o predecesor mporei na mhn yp;arxei
		NodeKey successorsKey = tmpAppNode.getSuccessor();
		try {
			System.out
					.println("find successor is trying to access remote's registry");
			Registry rmi = LocateRegistry.getRegistry(askingNode.getNodeKey()
					.getIP());
			if (rmi != null) {
				System.out
						.println("find successor gained access to remote's registry");
			} else {
				System.out.println("find successor NULL remote registry");
			}

			// System.out.println("find successor is looking up for "+"rmi:/" +
			// successorsKey.getIP() + ":1099/" +
			// String.valueOf(successorsKey.getPID()));
			System.out.println("find successor is looking up for " + "/"
					+ successorsKey.getIP() + ":1099/"
					+ String.valueOf(successorsKey.getPID()));

			// return (AppNodeInt) Naming.lookup("rmi:/" + successorsKey.getIP()
			// + ":1099/" + String.valueOf(successorsKey.getPID()));
			// return (AppNodeInt)
			// Naming.lookup("/"+successorsKey.getIP()+":1099/"+String.valueOf(successorsKey.getPID()));
			return (AppNodeInt) rmi.lookup(String.valueOf(successorsKey
					.getPID()));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static AppNodeInt find_predecessor(AppNodeInt askingNode,
			Key idToSearch) throws RemoteException, MalformedURLException {
		System.out.println("find predecessor is called ");
		AppNodeInt succ = null;
		AppNodeInt asking = askingNode;// pairnoume mia anfora
		// System.out.println(" find predecessor is trying to get asking's node rmi");
		// Registry remote=LocateRegistry.getRegistry("rmi://" +
		// asking.getNodeKey().getIP() + ":1099/");
		NodeKey succKey = askingNode.getSuccessor();
		Registry rmi = LocateRegistry.getRegistry(askingNode.getNodeKey()
				.getIP());
		if (rmi != null) {
			System.out
					.println("find predecessor gained access to remote's registry");
		} else {
			System.out.println("find predecessor NULL remote registry");
		}
		try {
			// mia ka8etos einai arketi gt i ip exei alli mia
			// System.out.println("find predeseccor Looking up for "+"rmi:/" +
			// succKey.getIP() + ":1099/" +String.valueOf(succKey.getPID())+"\n"
			// );
			System.out.println("find predeseccor Looking up for " + "/"
					+ succKey.getIP() + ":1099/"
					+ String.valueOf(succKey.getPID()) + "\n");
			// succ = (AppNodeInt) Naming.lookup("/" + succKey.getIP() +
			// ":1099/" +String.valueOf(succKey.getPID()));
			succ = (AppNodeInt) rmi.lookup(String.valueOf(succKey.getPID()));
			// succ = (AppNodeInt) Naming.lookup("rmi:/" + succKey.getIP() +
			// ":1099/" +String.valueOf(succKey.getPID()));
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AppNodeInt current = null;

		while (!Key.isBetweenSuccessor(idToSearch, askingNode.getNodeKey(),
				askingNode.getSuccessor())) {
			System.out
					.println("It is not between askingNode and askingNode's successor");

			if (!(current == null)) {
				System.out.println("!current==null");

				if (current.getNodeKey().equals(askingNode.getNodeKey())) {

					System.out
							.println("current key equals asking key, i am about to break");
					break;
				}

			}
			current = askingNode;
			// pairnoume pinaka daktulwn
			askingNode = bestFinger(askingNode, idToSearch);
			System.out.println("new asking Node is " + askingNode.getNodeKey());

			try {
				NodeKey k = askingNode.getSuccessor();
				Registry rmi2 = LocateRegistry.getRegistry(k.getIP());
				if (rmi2 != null) {
					System.out
							.println("find predecessor gained access to remote's registry in while");
				} else {
					System.out
							.println("find predecessor NULL remote registry in while");
				}
				// System.out.println("in while loop, lookin up for: "+"rmi:/" +
				// k.getIP() + ":1099/" +
				// String.valueOf(asking.getSuccessor().getPID()));
				// System.out.println("in while loop, lookin up for: "+"/" +
				// k.getIP() + ":1099/" +
				// String.valueOf(asking.getSuccessor().getPID()));
				// succ=(AppNodeInt) Naming.lookup("/" + k.getIP() + ":1099/" +
				// String.valueOf(asking.getSuccessor().getPID()));
				succ = (AppNodeInt) rmi2.lookup(String.valueOf(askingNode
						.getSuccessor().getPID()));

				// succ=(AppNodeInt) Naming.lookup("rmi:/" + k.getIP() +
				// ":1099/" + String.valueOf(asking.getSuccessor().getPID()));
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("find predecessor returns: "
				+ askingNode.getNodeKey() + "\n");

		return askingNode;

	}

	private static AppNodeInt bestFinger(AppNodeInt asking, Key idToSearch)
			throws RemoteException, MalformedURLException {
		// TODO Auto-generated method stub
		NodeKey finger = null;
		if (asking.getFingerList().size() == 0) {
			System.out.println("\nfinger list is 0 size");

			return asking;// can't do anything better
		}
		for (int i = (asking.getFingerList().size() - 1); i >= 0; i--) {
			// ta daktula anpoda
			finger = asking.getFinger(i);
			if (finger == null) {
				// den masame
				continue;
			}
			if (Key.isBetween(idToSearch, finger, asking.getNodeKey())) {
				try {
					Registry rmi2 = LocateRegistry.getRegistry(finger.getIP());
					if (rmi2 != null) {
						System.out
								.println("find predecessor gained access to remote's registry in while");
					} else {
						System.out
								.println("find predecessor NULL remote registry in while");
					}

					// return (AppNodeInt) Naming.lookup("/" + finger.getIP() +
					// ":1099/" + String.valueOf(finger.getPID()));
					return (AppNodeInt) rmi2.lookup(String.valueOf(finger
							.getPID()));

					// return (AppNodeInt) Naming.lookup("rmi:/" +
					// finger.getIP() + ":1099/" +
					// String.valueOf(finger.getPID()));
				} catch (NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		return asking;
	}

	public static void redistribute(AppNodeInt nodeToDistribute)
			throws RemoteException {
		// kratima anaforwn pre kai suc
		AppNode node = (AppNode) nodeToDistribute;
		NodeKey pre = node.getPredecessor();
		NodeKey suc = node.getSuccessor();

		// dinoume ta fileparts ston succesor

		// lokk up gia preNode kai sucNode na sigoureutoume oti uparxoun
		// kleinoume to daktulio
		// sucNode.predecessor=pre
		// preNode.successor=suc
		// pre.delete(1);

	}

}
