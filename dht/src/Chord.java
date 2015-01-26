

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;








import Hash.Key;
import Hash.NodeKey;

public class Chord implements Remote {
	/*
	 * new chord ring starts with with the specified node
	 */
	public static void create(AppNode appNode) {
		// TODO Auto-generated method stub
		
		appNode.setConnected(true);
		appNode.setSuccessor(appNode.getNodeID());//o eautos tou
		appNode.setPredecessor(null);
		
	}
	/*checkarei an exoume pre kai i an o pre pou dinoume einai pio konta
	 * 
	 */
	public static void notify(AppNode thisNode, AppNode possiblePre) {
		if(thisNode.getPredecessor()==null||Key.isBetweenNotify(possiblePre.getNodeID(), thisNode.getPredecessor(), thisNode.getNodeID())) {
			thisNode.setPredecessor(possiblePre.getNodeID());
		}
	}
	
	public static void join(AppNode thisNode, AppNode entryPointNode ) {
		thisNode.setPredecessor(null);
		NodeKey successorKey=null;
		successorKey=entryPointNode.find_successor_Key(thisNode.getNodeID());
		thisNode.setSuccessor(successorKey);
		thisNode.addFinger(0, successorKey);
		//zhtame ton successor
		try {
			AppNode n = (AppNode) Naming.lookup("/" + entryPointNode.getNodeID().getIP() + ":1099/" + String.valueOf(successorKey.getPID()));
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
	
		
	}
	public static AppNodeInt find_successor(AppNode askingNode, Key idToSearch) throws MalformedURLException, NotBoundException, RemoteException  {
		AppNodeInt tmpAppNode=find_predecessor(askingNode, idToSearch);
		//exoume ton proigoumeno.o epomenos tou proigoumenou einai o successor pou psaxnoume
		NodeKey successorsKey=tmpAppNode.getSuccessor();
		try {
			Registry rmi=LocateRegistry.getRegistry("rmi:/" + askingNode.getNodeID().getIP());
			return (AppNodeInt) Naming.lookup("/" + successorsKey.getIP() + ":1099/" + String.valueOf(successorsKey.getPID()));

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	public static AppNodeInt find_predecessor(AppNode askingNode, Key idToSearch) throws RemoteException {
		
		AppNodeInt succ=null;
		AppNodeInt asking=askingNode;//pairnoume mia anfora
		
			Registry remote=LocateRegistry.getRegistry("rmi:/" + asking.getNodeID().getIP() + ":1099/");
			NodeKey succKey=askingNode.getSuccessor();
			try {
				succ = (AppNodeInt) Naming.lookup("/" + succKey.getIP() + ":1099/" +String.valueOf(succKey.getPID()));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			AppNodeInt current=null;
			
			while(!Key.isBetween(idToSearch, asking.getNodeID(), asking.getSuccessor())) {
				if(!(current==null)) {
					if(current.getNodeID().equals(asking.getNodeID())) {
						break;
					}
				}
				current=asking;
				//pairnoume pinaka daktulwn
				asking= bestFinger(asking, idToSearch);
				
				try {
					NodeKey k=asking.getSuccessor();
					succ=(AppNodeInt) Naming.lookup("/" + k.getIP() + ":1099/" + String.valueOf(asking.getSuccessor().getPID()));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return asking;

			}
		
		
		
	
	private static AppNodeInt bestFinger(AppNodeInt asking, Key idToSearch) throws RemoteException {
		// TODO Auto-generated method stub
		NodeKey finger=null;
		if(asking.getFingerList().size()==0) {
			return asking;//can't do anything better
		}
		for(int i=(asking.getFingerList().size()-1);i>=0;i--) {
			//ta daktula anpoda
			finger=asking.getFinger(i);
			if(finger==null) {
				//den masame		
				continue;						
			}
			if(Key.isBetween(idToSearch, finger, asking.getNodeID())) {
				try {
					return (AppNodeInt) Naming.lookup("/" + finger.getIP() + ":1099/" + String.valueOf(finger.getPID()));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		
		
		
		return asking;
	}
}
