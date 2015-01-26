import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Hash.Key;
import Hash.NodeKey;


public class AppNode extends UnicastRemoteObject implements AppNodeInt{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5133746177914012364L;
	
	private final NodeKey NodeID;
	private boolean connected = false;
	private List<Key> fingers;
	private NodeKey predecessor;
	private NodeKey successor;
	private AppNodeSignature signature;
	private Map<Key, Filepart> entries;
	
	public AppNode(NodeKey key) throws RemoteException{
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
	public NodeKey getSuccessor(Key Key) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public NodeKey[] getFingers() throws RemoteException {
		// TODO Auto-generated method stub
		return (NodeKey[]) fingers.toArray();
	}
	@Override
	public NodeKey getFinger(int index) throws RemoteException {
		// TODO Auto-generated method stub
		return (NodeKey) fingers.get(index);
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
	
	public NodeKey getNodeID() {
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
	public void setPredecessor(NodeKey predecessor) {
		this.predecessor = predecessor;
	}
	public NodeKey getSuccessor() {
		return successor;
	}
	public void setSuccessor(NodeKey successor) {
		this.successor = successor;
	}
	public boolean isConnected() {
		return this.connected;
		
	}
	public void setConnected(boolean t) {
		connected=t;
		
	}
	public NodeKey find_successor_Key(Key nodeID2) {
		// TODO Auto-generated method stub
		return null;
	}
	public AppNodeInt find_successor(Key nodeID2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void addFinger(int i, Key successorKey) {
		// TODO Auto-generated method stub
		fingers.add(i, successorKey);
		
	}
	@Override
	public List getFingerList() {
		// TODO Auto-generated method stub
		return this.fingers;
	}
	
	

}
