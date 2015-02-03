package DistributedStorageApp;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import File.FilePart;
import Hash.Key;
import Hash.NodeKey;

/*
 * periexei tis apokrumasmenes kliseis pou dexeai o ka8e peer
 */
public interface AppNodeInt extends Remote {

	// public void addEntries(NodeKey key, Set

	public void addEntry(Key Key, FilePart entry) throws RemoteException; //unsed for now
	public boolean answer() throws RemoteException;

	public void addSuccessor(NodeKey key, int index) throws RemoteException;

	public void leave() throws RemoteException;//unsed for now IMPLEMENTED IN FIRST LEVEL 

	public FilePart getEntry(Key key) throws RemoteException;//unsed for now

	public NodeKey getFinger(int index) throws RemoteException;

	public List<NodeKey> getFingerList() throws RemoteException;

	public NodeKey[] getFingers() throws RemoteException;

	public NodeKey getNodeKey() throws RemoteException;

	// public void setPredecessor(AppNodeInt pr);
	public NodeKey getPredecessor() throws RemoteException;

	public NodeKey getSuccessor() throws RemoteException;

	// public NodeKey getSuccessor(Key Key) throws RemoteException;

	public void join(AppNodeInt newNode) throws RemoteException;

	public void setFinger(int index, NodeKey key) throws RemoteException;

	public void start() throws RemoteException;

	public void addFinger(int i, Key successorKey) throws RemoteException;//unsed for now

	public AppNodeInt find_successor(Key nodeID2) throws RemoteException;//unsed for now

	public NodeKey find_successor_NodeID(Key nodeID2) throws RemoteException;	

	public int getFingersSize() throws RemoteException;

	public List<NodeKey> getSuccessorList() throws RemoteException;

	public int getSuccessorListSize() throws RemoteException;

	public boolean isConnected() throws RemoteException;

	public void setConnected(boolean t) throws RemoteException;

	void setPredecessor(NodeKey predecessor) throws RemoteException;

	void setSuccessor(NodeKey successor) throws RemoteException; //JUST IN CASE

	void setSignature(AppNodeSignature thisSignature) throws RemoteException;

	public void clearFingers() throws RemoteException;

	public AppNodeSignature getSignature() throws RemoteException;

	public void deleteSuccessor(int i) throws RemoteException;//unsed for now

	public void deleteSuccessors() throws RemoteException;

}
