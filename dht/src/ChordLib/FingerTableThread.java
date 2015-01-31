package ChordLib;

import java.math.BigInteger;
import java.rmi.RemoteException;

import DistributedStorageApp.AppNode;
import DistributedStorageApp.AppNodeInt;
import Hash.NodeKey;
import Hash.Number160Bit;

public class FingerTableThread implements Runnable {
	
	private AppNodeInt node;
	private int fingerToCheck;
	public FingerTableThread(AppNodeInt node) {
		this.node=node;
		fingerToCheck=0;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			System.out.println("Trying to run fingerTableThread on "+node.getNodeKey().toString());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fingerToCheck++;
		if(fingerToCheck>AppNode.FINGERS_ON_NODE) {
			fingerToCheck=1;//xreiazetai meta 
		}
		NodeKey finger=null;
		 // called periodically. refreshes finger table entries.
	/*	 // next stores the index of the finger to fix
		 n.fix_fingers()
		   next = next + 1;
		   if (next > m)
		     next = 1;
		   finger[next] = find_successor(n+2^{next-1});*/
		BigInteger two=BigInteger.valueOf(new Integer(2));
		
		Integer next_plus_one_power_of_two=(int) Math.pow(2, fingerToCheck-1);//oti leei i 8ewria
		try {
			Number160Bit t=node.getNodeKey().getKeyValue().add(BigInteger.valueOf(next_plus_one_power_of_two));
			NodeKey arg_of_findsuccessor=new NodeKey(t, node.getNodeKey().getPID(), node.getNodeKey().getIP());
		
			if(arg_of_findsuccessor.equals(node.getNodeKey())) {
				return;//nothing to do
			}
			//an ftasei mexri edw 
			finger=node.find_successor_NodeID(arg_of_findsuccessor);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(finger!=null) {
			//an den epistrepsei null einai to neo daktulo
			try {
				node.setFinger(finger, fingerToCheck-1);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			fingerToCheck=0;
		}
		
	}

}
