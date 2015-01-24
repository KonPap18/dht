import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Hash.Key;


public class AppNode extends UnicastRemoteObject implements AppNodeInt{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5133746177914012364L;
	
	private final Key NodeID;
	private boolean connected = false;
	private List<Key> fingers;
	private Key predecessor;
	private Key successor;
	private AppNodeSignature signature;
	private Map<Key, Filepart> entries;
	
	public AppNode(Key key) throws RemoteException{
		// TODO Auto-generated constructor stub
		fingers=new ArrayList(6);
		NodeID=key;
		predecessor=null;
		successor=null;
		entries=new HashMap<Key, Filepart>();
		
		
	}
	@Override
	public void start() {
		// TODO Auto-generated method stub
		Chord.create(this);
		
		
	}
	@Override
	public void join(AppNode newNode) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void disconnect() throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Key getSuccessor(Key Key) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Key[] getFingers() throws RemoteException {
		// TODO Auto-generated method stub
		return (Key[]) fingers.toArray();
	}
	@Override
	public Key getFinger(int index) throws RemoteException {
		// TODO Auto-generated method stub
		return fingers.get(index);
	}
	@Override
	public void setFinger(Key key, int index) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public synchronized void addEntry(Key Key, Filepart entry) throws RemoteException {		
		entries.put(Key,  entry);
		
	}
	@Override
	public Filepart getEntry(Key key) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	public Key getNodeID() {
		// TODO Auto-generated method stub
		return this.NodeID;
	}
	public void setSignature(AppNodeSignature thisSignature) {
		// TODO Auto-generated method stub
		signature=thisSignature;
		
	}
	@Override
	public void join(AppNodeInt newNode) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	public Key getPredecessor() {
		return predecessor;
	}
	public void setPredecessor(Key predecessor) {
		this.predecessor = predecessor;
	}
	public Key getSuccessor() {
		return successor;
	}
	public void setSuccessor(Key successor) {
		this.successor = successor;
	}
	public boolean isConnected() {
		return this.connected;
		
	}
	public void setConnected(boolean t) {
		connected=t;
		
	}
	public Key find_successor(Key nodeID2) {
		// TODO Auto-generated method stub
		return null;
	}
	public void addFinger(int i, Key successorKey) {
		// TODO Auto-generated method stub
		fingers.add(i, successorKey);
		
	}
	

}
