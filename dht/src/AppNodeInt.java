import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import Hash.Key;
import Hash.NodeKey;



/*
 * periexei tis apokrumasmenes kliseis pou dexeai o ka8e peer
 */
public interface AppNodeInt  extends Remote{
	public void start();
	public void join(AppNodeInt newNode) throws RemoteException;
	public void disconnect() throws RemoteException;
	public NodeKey getSuccessor(Key Key) throws RemoteException;
	public NodeKey[] getFingers() throws RemoteException;
	public NodeKey getFinger(int index) throws RemoteException;
	public void setFinger(Key key, int index) throws RemoteException;
	public void addEntry(Key Key, Filepart entry) throws RemoteException;
	public Filepart getEntry(Key key) throws RemoteException;
	void join(AppNode newNode) throws RemoteException;
	public NodeKey getSuccessor();
	public NodeKey getNodeID();
	public List getFingerList();
}

