package DistributedStorageApp;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import File.Filepart;
import Hash.Key;
import Hash.NodeKey;

/*
 * periexei tis apokrumasmenes kliseis pou dexeai o ka8e peer
 */
public interface AppNodeInt extends Remote {
	public void start() throws RemoteException;

	public void join(AppNodeInt newNode) throws RemoteException;

	public void disconnect() throws RemoteException;

	public NodeKey getSuccessor(Key Key) throws RemoteException;

	public NodeKey[] getFingers() throws RemoteException;

	public NodeKey getFinger(int index) throws RemoteException;

	public void setFinger(Key key, int index) throws RemoteException;

	public void addEntry(Key Key, Filepart entry) throws RemoteException;

	public Filepart getEntry(Key key) throws RemoteException;

	public NodeKey getSuccessor() throws RemoteException;

	public NodeKey getNodeKey() throws RemoteException;

	public List getFingerList() throws RemoteException;

	// public void setPredecessor(AppNodeInt pr);
	public NodeKey getPredecessor() throws RemoteException;

	void setPredecessor(NodeKey predecessor) throws RemoteException;

	void setSuccessor(NodeKey successor) throws RemoteException;

	boolean isConnected() throws RemoteException;

	void setConnected(boolean t) throws RemoteException;

	NodeKey find_successor_NodeID(Key nodeID2) throws RemoteException;

	AppNodeInt find_successor(Key nodeID2) throws RemoteException;

	void addFinger(int i, Key successorKey) throws RemoteException;

	public boolean answer() throws RemoteException;
}
