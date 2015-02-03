package DistributedStorageApp;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import ChordLib.CheckPredecessorThread;
import ChordLib.Chord;
import ChordLib.FingerTableThread;
import ChordLib.StabilizerThread;
import File.FilePart;
import Hash.Key;
import Hash.NodeKey;

public class AppNode extends UnicastRemoteObject implements AppNodeInt {

	/**
	 *
	 */
	private static final long serialVersionUID = 5133746177914012364L;

	public static final int FINGERS_ON_NODE = 6;

	private ScheduledExecutorService nodeScheduledExecutor;

	private final NodeKey NodeID;
	private boolean connected = false;
	private List<NodeKey> fingers;
	private List<NodeKey> successorList;
	private NodeKey predecessor;
	// private NodeKey successor;
	private Map<Key, FilePart> entries;

	private AppNodeSignature signature;

	private ScheduledFuture<?> checkPredecessorScheduledFuture;
	private ScheduledFuture<?> stabilizerScheduledFuture;
	private ScheduledFuture<?> fingerTableScheduledFuture;

	public AppNode(NodeKey key) throws RemoteException {

		fingers = new ArrayList(FINGERS_ON_NODE);
		NodeID = key;
		predecessor = null;
		// successor = null;
		successorList = new ArrayList<NodeKey>();
		entries = new HashMap<Key, FilePart>();

	}

	@Override
	public List<NodeKey> getSuccessorList() {
		return this.successorList; // OK
	}

	@Override
	public int getSuccessorListSize() {
		return this.successorList.size(); // OK
	}

	@Override
	public synchronized void addEntry(Key Key, FilePart entry)
			throws RemoteException {
		// LOT MORE TO DO
		entries.put(Key, entry);

	}

	@Override
	public void setFinger(int i, NodeKey fing) throws RemoteException {

		// fingers.add(i, (NodeKey) fing); // mipws na alla3ei to orisma se
		// pio specific anti gia cast
		if (i > FINGERS_ON_NODE - 1) {
			return;
		}
		if (fing.equals(NodeID)) {
			return; // dont add yourself
		}
		if (fingers.contains(fing)) {
			return; // dont add second time
		}
		fingers.add(fing);
		Collections.sort(fingers);

	}

	@Override
	public void leave() throws RemoteException {
		Chord.redistribute(this);

	}

	@Override
	public AppNodeInt find_successor(Key nodeID2) throws RemoteException {

		try {
			return Chord.find_successor(this, nodeID2);
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (NotBoundException e) {

			e.printStackTrace();
		}
		return null;
	}

	@Override
	public NodeKey find_successor_NodeID(Key nodeID2) throws RemoteException {
		NodeKey ret = null;
		// κληση πισω στο chord
		System.out.println("Node " + this.getNodeKey().getPID()
				+ " is calling method find successor from chord for node "
				+ nodeID2.toString());
		try {
			ret = Chord.find_successor(this, nodeID2).getNodeKey();

		} catch (RemoteException e) {

			e.printStackTrace();
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (NotBoundException e) {

			e.printStackTrace();
		}

		return ret;
	}

	@Override
	public FilePart getEntry(Key key) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeKey getFinger(int index) throws RemoteException {

		return fingers.get(index);
	}

	@Override
	public List getFingerList() {
		return fingers;
	}

	@Override
	public NodeKey[] getFingers() throws RemoteException {
		return (NodeKey[]) fingers.toArray();
	}

	@Override
	public NodeKey getNodeKey() {
		return NodeID;
	}

	@Override
	public NodeKey getPredecessor() {
		return predecessor;
	}

	/*
	 * @Override public NodeKey getSuccessor() { return successor; }
	 */

	@Override
	public boolean isConnected() throws RemoteException {
		return connected;

	}

	@Override
	public void join(AppNodeInt entryPointNode) throws RemoteException {
		System.out.println("Joining..");
		Chord.join(this, entryPointNode);
		System.out.println("Finaly this node has successor: "
				+ entryPointNode.getSuccessor().getPID()/*
														 * +" and predecessor "+
														 * entryPointNode
														 * .getPredecessor
														 * ().getPID()
														 */);

		nodeScheduledExecutor = Executors.newScheduledThreadPool(3);
		checkPredecessorScheduledFuture = nodeScheduledExecutor
				.scheduleAtFixedRate(new CheckPredecessorThread(
						(AppNodeInt) this), 20, 5, TimeUnit.SECONDS);
		stabilizerScheduledFuture = nodeScheduledExecutor.scheduleAtFixedRate(
				new StabilizerThread(this), 3, 2, TimeUnit.SECONDS);
		fingerTableScheduledFuture = nodeScheduledExecutor.scheduleAtFixedRate(
				new FingerTableThread(this), 3, 4, TimeUnit.SECONDS);

	}

	@Override
	public void setConnected(boolean t) throws RemoteException {
		connected = t;

	}

	@Override
	public int getFingersSize() {
		return fingers.size();
	}

	@Override
	public void setPredecessor(NodeKey predecessor) {
		this.predecessor = predecessor;
	}

	@Override
	public void setSuccessor(NodeKey successor) throws RemoteException {
		this.fingers.add(0, successor);
	}

	@Override
	public void start() {
		System.out.println("Node trying to start..");
		Chord.createRing(this);
		// threads must start somewhere here
		nodeScheduledExecutor = Executors.newScheduledThreadPool(3);
		checkPredecessorScheduledFuture = nodeScheduledExecutor
				.scheduleAtFixedRate(new CheckPredecessorThread(this), 10, 5,
						TimeUnit.SECONDS);
		stabilizerScheduledFuture = nodeScheduledExecutor.scheduleAtFixedRate(
				new StabilizerThread(this), 3, 2, TimeUnit.SECONDS);
		fingerTableScheduledFuture = nodeScheduledExecutor.scheduleAtFixedRate(
				new FingerTableThread(this), 2, 3, TimeUnit.SECONDS);
	}

	@Override
	public boolean answer() {
		return true;// just return true
	}

	@Override
	public void addSuccessor(NodeKey key, int index) throws RemoteException {
		synchronized (this.successorList) {
			for (int i = 0; i < successorList.size(); i++) {
				if (key.equals(successorList.get(i))) {
					return;
				}
			}

			successorList.add(index, key);
			if (this.successorList.size() > 3) {
				for (int i = successorList.size() - 1; i > 2; i--) {
					successorList.remove(i);
				}
			}
		}

	}

	@Override
	public NodeKey getSuccessor() throws RemoteException {
		return successorList.get(0);
	}

	@Override
	public void addFinger(int i, Key successorKey) throws RemoteException {
		// TODO Auto-generated method stub
	}

	@Override
	public void setSignature(AppNodeSignature thisSignature) {
		this.signature = thisSignature;

	}

	@Override
	public void clearFingers() throws RemoteException {

		for (int i = 0; i < fingers.size(); i++) {
			fingers.remove(i);
		}
	}

	@Override
	public AppNodeSignature getSignature() throws RemoteException {
		// TODO Auto-generated method stub
		return this.signature;
	}

	@Override
	public void deleteSuccessor(int i) throws RemoteException {
		synchronized (this.successorList) {
			if (i < successorList.size() && i >= 0) {
				successorList.remove(i);
			}
		}

	}

	@Override
	public void deleteSuccessors() throws RemoteException {
		for (int i = 0; i < successorList.size(); i++) {
			successorList.remove(i);
		}

	}

}
