package ChordLib;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Hash.Key;

public class ChordNodeImpl implements Remote, ChordNode {
	
	private Key nodeId;
	
	@Override
	public void create() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyNode(ChordNode p) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void join(ChordNode to) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ChordNode find_succesor(Key k) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Key getPredecessor() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPredecessor(Key idKey) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Key[] getFingers() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Key getFinger(int index) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeFinger(int index) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	

}
