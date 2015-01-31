package DistributedStorageApp;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import ChordLib.Chord;
import File.Filepart;
import Hash.Key;
import Hash.NodeKey;

public class AppNode extends UnicastRemoteObject implements AppNodeInt {

	/**
	 *
	 */
	private static final long serialVersionUID = 5133746177914012364L;

	public static final int FINGERS_ON_NODE = 6;

	private final NodeKey NodeID;
	private boolean connected = false;
	private List<NodeKey> fingers;
	private NodeKey predecessor;
	private NodeKey successor;
	private Map<Key, Filepart> entries;

	private ScheduledExecutorService ThreadScheduler;

	public AppNode(NodeKey key) throws RemoteException {

		fingers = new ArrayList(FINGERS_ON_NODE);
		NodeID = key;
		predecessor = null;
		successor = null;
		entries = new HashMap<Key, Filepart>();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see AppNodeInt#start()
	 */

	@Override
	public synchronized void addEntry(Key Key, Filepart entry)
			throws RemoteException {
		entries.put(Key, entry);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see AppNodeInt#addFinger(int, Hash.Key)
	 */
	@Override
	public void addFinger(int i, Key successorKey) throws RemoteException {

		fingers.add(i, (NodeKey) successorKey); // mipws na alla3ei to orisma se
		// pio specific anti gia cast

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see AppNodeInt#getSuccessor(Hash.Key)
	 */

	@Override
	public void disconnect() throws RemoteException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see AppNodeInt#getFingers()
	 */

	/*
	 * (non-Javadoc)
	 *
	 * @see AppNodeInt#find_successor(Hash.Key)
	 */
	@Override
	public AppNodeInt find_successor(Key nodeID2) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see AppNodeInt#getFinger(int)
	 */

	/*
	 * (non-Javadoc)
	 *
	 * @see AppNodeInt#find_successor_NodeID(Hash.Key)
	 */
	@Override
	public NodeKey find_successor_NodeID(Key nodeID2) throws RemoteException {
		NodeKey ret=null;
		//κληση πισω στο chord
		System.out.println("Node "+ this.getNodeKey().getPID()+" is calling method find successor from chord for node "+nodeID2.toString());
		try {
			ret=Chord.find_successor(this, nodeID2).getNodeKey();
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see AppNodeInt#setFinger(Hash.Key, int)
	 */

	@Override
	public Filepart getEntry(Key key) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see AppNodeInt#addEntry(Hash.Key, Filepart)
	 */

	@Override
	public NodeKey getFinger(int index) throws RemoteException {
		// TODO Auto-generated method stub
		return fingers.get(index);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see AppNodeInt#getEntry(Hash.Key)
	 */

	@Override
	public List getFingerList() {
		// TODO Auto-generated method stub
		return fingers;
	}

	@Override
	public NodeKey[] getFingers() throws RemoteException {
		// TODO Auto-generated method stub
		return (NodeKey[]) fingers.toArray();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see AppNodeInt#getNodeID()
	 */
	@Override
	public NodeKey getNodeKey() {
		// TODO Auto-generated method stub
		return NodeID;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see AppNodeInt#join(AppNodeInt)
	 */

	@Override
	public NodeKey getPredecessor() {
		return predecessor;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see AppNodeInt#getSuccessor()
	 */
	@Override
	public NodeKey getSuccessor() {
		return successor;
	}

	@Override
	public NodeKey getSuccessor(Key Key) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see AppNodeInt#isConnected()
	 */
	@Override
	public boolean isConnected() throws RemoteException {
		return connected;

	}

	@Override
	public void join(AppNodeInt entryPointNode) throws RemoteException {
		System.out.println("Joining..");
		Chord.join(this, entryPointNode);
		System.out.println("Finaly this node has successor: "+entryPointNode.getSuccessor().getPID()/*+" and predecessor "+entryPointNode.getPredecessor().getPID()*/ );
		//ThreadScheduler = Executors.newScheduledThreadPool(3);
	
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see AppNodeInt#setConnected(boolean)
	 */
	@Override
	public void setConnected(boolean t) throws RemoteException {
		connected = t;

	}

	@Override
	public void setFinger(Key key, int index) throws RemoteException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see AppNodeInt#setPredecessor(Hash.NodeKey)
	 */
	@Override
	public void setPredecessor(NodeKey predecessor) {
		this.predecessor = predecessor;
	}

	public void setSignature(AppNodeSignature thisSignature) {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see AppNodeInt#getFingerList()
	 */

	/*
	 * (non-Javadoc)
	 *
	 * @see AppNodeInt#setSuccessor(Hash.NodeKey)
	 */
	@Override
	public void setSuccessor(NodeKey successor) throws RemoteException {
		this.successor = successor;
	}

	@Override
	public void start() {
		System.out.println("Node trying to start..");
		Chord.create(this);
		// threads must start somewhere here

	}

	@Override
	public boolean answer() {
		// TODO Auto-generated method stub
		return true;
	}

}
