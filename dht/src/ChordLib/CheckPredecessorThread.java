package ChordLib;

import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.RemoteException;

import DistributedStorageApp.AppNodeInt;

public class CheckPredecessorThread implements Runnable {
	private final AppNodeInt node;
	private boolean nodeAnswered;
	
	public CheckPredecessorThread(AppNodeInt node) {
		this.node=node;
	}
	
	@Override
	public void run() {
		AppNodeInt tempPre=null;
		
		try {
			if(node.getPredecessor()==null) {
				return;//no pred
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//αν δεν ειναι null
		try {
			tempPre=(AppNodeInt)Naming.lookup("rmi:/"+node.getPredecessor().getIP()+":1099/"+String.valueOf(node.getPredecessor().getPID()));
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		
		
		if(tempPre!=null) {
			try {
				nodeAnswered=node.answer();
				System.out.println("my predeseccor "+tempPre.getNodeKey().toString()+" is alive");
			} catch (ConnectException e) {
				System.out.println("my pred is dead or something");
				try {
					node.setPredecessor(null);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(!nodeAnswered) {
				//pre is dead
				try {
					node.setPredecessor(null);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		nodeAnswered=false;
		

	}

}
