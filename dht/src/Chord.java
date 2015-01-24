

import java.rmi.Remote;

import Hash.Key;

public class Chord implements Remote {
	/*
	 * new chord ring starts with with the specified node
	 */
	public static void create(AppNode appNode) {
		// TODO Auto-generated method stub
		
		appNode.setConnected(true);
		appNode.setSuccessor(appNode.getNodeID());//o eautos tou
		appNode.setPredecessor(null);
		
	}
	/*checkarei an exoume pre kai i an o pre pou dinoume einai pio konta
	 * 
	 */
	public static void notify(AppNode thisNode, AppNode possiblePre) {
		if(thisNode.getPredecessor()==null|| Key.isBetweenNotify(possiblePre.getNodeID(), thisNode.getPredecessor())) {
			thisNode.setPredecessor(possiblePre.getNodeID());
		}
	}
	
	public static void join(AppNode thisNode, AppNode entryPointNode ) {
		thisNode.setPredecessor(null);
		Key successorKey=null;
		successorKey=entryPointNode.find_successor(thisNode.getNodeID());
		thisNode.setSuccessor(successorKey);
		thisNode.addFinger(0, successorKey);
	}

}
