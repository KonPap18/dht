import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import Hash.Key;


public class ChordClient {
	static Registry rmiRegistry;
	static AppNode thisNode;
	
	
	public static void main(String[]args) throws IOException {
		InetAddress thisIP = null;
		rmiRegistry=null;
		int processID;
		AppNodeSignature thisSignature;
		
		String NodeProcessID=ManagementFactory.getRuntimeMXBean().getName();
		processID=Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().substring(0, ManagementFactory.getRuntimeMXBean().getName().indexOf("@")));
		//System.out.println(NodeProcessID);
		
		AppNodeSignature luckyPeer;
		try {
			thisIP=IPTools.getIP();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(thisIP);
		System.out.println(thisIP.getHostAddress());
	
		thisNode=new AppNode(new Key(processID, thisIP.toString()));
		System.setProperty("java.rmi.server.hostname", thisIP.getHostAddress());

		
		
		
		
		/*
		 *do things to select files to share
		 *start file server not implemented yet
		 * 
		 * */
		
		 //setarisma komvou
		thisSignature=new AppNodeSignature(thisIP, processID, thisNode.getNodeID().toString());
		thisNode.setSignature(thisSignature);
		
		/*Create RMI Registry*/
		try
		{
			rmiRegistry = LocateRegistry.createRegistry(1099);
			
		}
		catch (java.rmi.server.ExportException ee)
		{
			rmiRegistry = LocateRegistry.getRegistry();
			//Log.addMessage("Registered on local rmi server", Log.INFORMATION);
		}
		//prepei na vroume ena komvo 
		luckyPeer= Multicaster.findTheLuckyOne();
		
		//vrikame h den vrikame peer
		
		if(luckyPeer==null) {
			try {
				Naming.bind(thisSignature.getRmiRecord(), thisNode);
			}catch(RemoteException ex) {
				
			} catch (AlreadyBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			thisNode.start();
			
			
		}else {//vrikame ton tuxero
			Registry remoteReg = LocateRegistry.getRegistry(luckyPeer.getIp().getHostName());
			AppNodeInt firstPeer=null;
			
			
				try {
					firstPeer=(AppNodeInt)Naming.lookup(("//"+luckyPeer.getIp().getHostAddress()+" :1099/"+luckyPeer.getRmiRecord()));
				} catch (NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//mallon exoume parei to komvo. as grapsoume sto diko mas rmi 
				try {
					Naming.bind("//" + thisIP.getHostAddress() + ":1099/" + thisSignature.getProcessID(), thisNode);
				} catch (AlreadyBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			//na ton valoume ston daktulio
				thisNode.join(firstPeer);
		}
		//na ton baloyme na leei ta stoixeia tou
		SignatureResponse sigRes=new SignatureResponse(thisSignature);
		Thread sigResThread=new Thread(sigRes);
		sigResThread.start();
	}
}
