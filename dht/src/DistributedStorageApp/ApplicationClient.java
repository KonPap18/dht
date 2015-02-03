package DistributedStorageApp;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

import Hash.NodeKey;
import Misc.IPTools;
import Services.Multicaster;
import Services.SignatureResponse;

public class ApplicationClient {
	static Registry rmiRegistry;
	static AppNode thisNode;

	public static void main(String[] args) throws IOException {
		InetAddress thisIP = null;
		rmiRegistry = null;
		int processID;
		String hostName;
		AppNodeSignature thisSignature;

		String NodeProcessID = ManagementFactory.getRuntimeMXBean().getName();
		processID = Integer.parseInt(ManagementFactory
				.getRuntimeMXBean()
				.getName()
				.substring(
						0,
						ManagementFactory.getRuntimeMXBean().getName()
								.indexOf("@")));
		hostName = NodeProcessID.substring(NodeProcessID.indexOf("@") + 1,
				NodeProcessID.length());
		System.out.println("Process ID " + processID + " running on "
				+ hostName);

		AppNodeSignature luckyPeer;
		try {
			thisIP = IPTools.getIP();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Node IP: " + thisIP.getHostAddress());
		System.out.println("Trying to create Node...");
		thisNode = new AppNode(new NodeKey(processID, thisIP.getHostAddress()));
		System.out.println("Node created!");
		System.out.println("Node's key value: "
				+ thisNode.getNodeKey().getKeyValue().toString());

		Properties prop = System.getProperties();
		prop.setProperty("java.rmi.server.hostname", thisIP.getHostAddress());
		System.setProperties(prop);
		// System.setProperty("java.rmi.server.hostname",
		// thisIP.getHostAddress());

		/*
		 * property for multicasting
		 */
		Properties props = System.getProperties();
		props.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperties(props);

		/*
		 * do things to select files to sharestart file server not implemented
		 * yet
		 */

		// setarisma komvou
		thisSignature = new AppNodeSignature(thisIP, processID);
		thisNode.setSignature(thisSignature);
		System.out.println("Node's signature setted");
		/* Create RMI Registry */
		try {
			rmiRegistry = LocateRegistry.createRegistry(1099);

		} catch (java.rmi.server.ExportException ee) {
			rmiRegistry = LocateRegistry.getRegistry();
			// Log.addMessage("Registered on local rmi server",
			// Log.INFORMATION);
			System.out.println("An exception occured, just ignore it...");
		}
		System.out.println("Rmi created");
		// prepei na vroume ena komvo
		luckyPeer = Multicaster.findTheLuckyOne();
		// System.out.println(luckyPeer.getProcessID());
		// vrikame h den vrikame peer

		if (luckyPeer == null) {
			System.out.println("NO answer...");
			try {
				System.out.println("bind name for bootstrap "
						+ thisSignature.getRmiRecord());
				Naming.bind(thisSignature.getRmiRecord(), thisNode);
				// Naming.rebind("//:5099/count", v);
				System.out
						.println("Node's service binded.. this is a bootstrap node");

			} catch (RemoteException ex) {
				ex.printStackTrace();
			} catch (AlreadyBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// na ton baloyme na leei ta stoixeia tou
			SignatureResponse sigRes = new SignatureResponse(thisSignature);
			Thread sigResThread = new Thread(sigRes);
			sigResThread.start();
			System.out
					.println("Signature response thread started for bootstrap..");

			thisNode.start();

		} else {// vrikame ton tuxero
			System.out.println("Someone answered...");
			Registry remoteReg = LocateRegistry.getRegistry(luckyPeer.getIp()
					.getHostName());
			AppNodeInt firstPeer = null;

			try {
				// firstPeer=(AppNodeInt)
				// remoteReg.lookup("rmi://"+luckyPeer.getIp().getHostAddress()+":1099/"+luckyPeer.getRmiRecord());
				// /*to cremove rmi:!!*/String
				// nameToLookup="rmi://"+luckyPeer.getIp().getHostAddress()+":1099/"+luckyPeer.getRmiRecord();
				// firstPeer=(AppNodeInt)Naming.lookup(("//"+luckyPeer.getIp().getHostAddress()+":1099/"+luckyPeer.getRmiRecord()));
				// System.out.println("name to look up for NOT BOOTSTRAP"+
				// nameToLookup);
				System.out.println(luckyPeer.getRmiRecord());
				firstPeer = (AppNodeInt) remoteReg.lookup(luckyPeer
						.getRmiRecord());
				// firstPeer=(AppNodeInt)remoteReg.lookup("rmi://"+luckyPeer.getIp().getHostAddress()+":1099/"+luckyPeer.getRmiRecord());

				// firstPeer=(AppNodeInt) Naming.lookup(nameToLookup);
				System.out.println("found a node with process ID"
						+ firstPeer.getNodeKey().getPID()
						+ "running on unknown host on IP: "
						+ firstPeer.getNodeKey().getIP());
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// mallon exoume parei to komvo. as grapsoume sto diko mas rmi
			try {
				System.out.println("bind name for Non bootstrap "
						+ thisSignature.getRmiRecord());
				// System.out.println("bind name for NOT bootstrap"+"//" +
				// thisIP.getHostAddress() + ":1099/" +
				// thisSignature.getProcessID());
				// /*to cremove rmi:!!*/ Naming.bind("rmi://" +
				// thisIP.getHostAddress() + ":1099/" +
				// thisSignature.getProcessID(), thisNode);
				Naming.bind(thisSignature.getRmiRecord(), thisNode);
				System.out
						.println("Node's service binded.. this is NOT bootstrap");
			} catch (AlreadyBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SignatureResponse sigRes = new SignatureResponse(thisSignature);
			Thread sigResThread = new Thread(sigRes);
			sigResThread.start();
			System.out.println("Signature response thread started..");
			if (firstPeer == null) {
				System.out.println("!!!!!!!!!!!first peer == null!!!!!!!!!!");
			}
			// na ton valoume ston daktulio
			try {
				System.out.println("reached this point now must print from "
						+ firstPeer.getNodeKey());
				thisNode.join(firstPeer);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
