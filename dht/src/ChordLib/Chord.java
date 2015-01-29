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
	public static void create(AppNode appNode) {
		System.out.println("Chord trying to start node "+ appNode.getNodeKey());
		
		
		try {
			appNode.setConnected(true);
			appNode.setSuccessor(appNode.getNodeKey());
			//o eautos tou
			appNode.setPredecessor(null);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	/*checkarei an exoume pre kai i an o pre pou dinoume einai pio konta
	 * 
	 */
	public static void notify(AppNodeInt thisNode, AppNodeInt n) throws RemoteException{
		//check an o pre einai null, kai an o pi8anos pre einai anamesa sto kleidi tou kommvou kai ston twrino pre
		//an isxuei allazoume ton pre
		if(thisNode.getPredecessor()==null||Key.isBetweenNotify(n.getNodeKey(), thisNode.getPredecessor(), thisNode.getNodeKey())) {
			thisNode.setPredecessor(n.getNodeKey());
			System.out.println("Predecessor changed for node "+ thisNode.getNodeKey().getPID()+"new predecessor "+n.getNodeKey().getPID());
		}
	}
	
	public static void join(AppNodeInt thisNode, AppNodeInt entryPointNode ) throws RemoteException {
		System.out.println("Chord trying to join node "+thisNode.getNodeKey().getPID()+"withn entry point node "+entryPointNode.getNodeKey().getPID() );
		thisNode.setPredecessor(null);
		
		NodeKey successorKey=null;
		
		successorKey=entryPointNode.find_successor_NodeID(thisNode.getNodeKey());
		if(successorKey==null) {
			System.out.println("find successor is fucked up, returns null and shit");
		}
		thisNode.setSuccessor(successorKey);
		thisNode.addFinger(0, successorKey);
		//zhtame ton successor
		try {
			System.out.println("joining node is looking up for "+"rmi:/" + entryPointNode.getNodeKey().getIP() + ":1099/" + String.valueOf(successorKey.getPID()));
			
			AppNodeInt n = (AppNode) Naming.lookup("rmi:/" + entryPointNode.getNodeKey().getIP() + ":1099/" + String.valueOf(successorKey.getPID()));
			//n ειναι ο successor toy thisnode
			Chord.notify(thisNode, n);//ελεγχος αν ο επόμενος 
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
	public static AppNodeInt find_successor(AppNodeInt askingNode, Key idToSearch) throws MalformedURLException, NotBoundException, RemoteException  {
		System.out.println("Entered method find successor in chord.Caller is "+askingNode.getNodeKey().getPID());
		AppNodeInt tmpAppNode=find_predecessor(askingNode, idToSearch);
		//exoume ton proigoumeno.o epomenos tou proigoumenou einai o successor pou psaxnoume
		NodeKey successorsKey=tmpAppNode.getSuccessor();
		try {
			System.out.println("find successor is trying to access remote's registry");
			Registry rmi=LocateRegistry.getRegistry(askingNode.getNodeKey().getIP());
			System.out.println("find successor gained access to remote's registry");
			return (AppNodeInt) Naming.lookup("rmi:/" + successorsKey.getIP() + ":1099/" + String.valueOf(successorsKey.getPID()));

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	public static AppNodeInt find_predecessor(AppNodeInt askingNode, Key idToSearch) throws RemoteException  {
		System.out.println("find predecessor is called ");
		AppNodeInt succ=null;
		AppNodeInt asking=askingNode;//pairnoume mia anfora
		System.out.println("trying to get asking's node rmi");
			//Registry remote=LocateRegistry.getRegistry("rmi://" + asking.getNodeKey().getIP() + ":1099/");
			NodeKey succKey=askingNode.getSuccessor();
			try {
				//mia ka8etos einai arketi gt i ip exei alli mia
				System.out.println("Looking up for "+"rmi:/" + succKey.getIP() + ":1099/" +String.valueOf(succKey.getPID()) );
				succ = (AppNodeInt) Naming.lookup("rmi:/" + succKey.getIP() + ":1099/" +String.valueOf(succKey.getPID()));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			AppNodeInt current=null;
			
			while(!Key.isBetween(idToSearch, asking.getNodeKey(), asking.getSuccessor())) {
				if(!(current==null)) {
					if(current.getNodeKey().equals(asking.getNodeKey())) {
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
			if(Key.isBetween(idToSearch, finger, asking.getNodeKey())) {
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
