package ChordLib;

import java.rmi.RemoteException;

import Hash.Key;

public interface ChordNode {
	public void create() throws RemoteException;
	public void notifyNode(ChordNode p) throws RemoteException;
	public void join(ChordNode to) throws RemoteException;
	public ChordNode find_succesor(Key k) throws RemoteException;	
	public Key getPredecessor() throws RemoteException;
	public void setPredecessor(Key idKey) throws RemoteException;
	public Key[] getFingers() throws RemoteException;
	public Key getFinger(int index) throws RemoteException;
	public void removeFinger(int index) throws RemoteException;


}
