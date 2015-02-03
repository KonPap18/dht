package DistributedStorageApp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;

public class AppNodeSignature implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1856331294111958613L;
	private InetAddress Ip;
	private int processID;
	private String rmiRecord;// pvw ua eggrafei sto rmi

	public AppNodeSignature(InetAddress ip, int processID, String rmiRecord) {
		Ip = ip;
		this.processID = processID;
		// this.rmiRecord = String.valueOf(processID);
		this.rmiRecord = rmiRecord;
	}

	public AppNodeSignature(InetAddress thisIP, int processID2) {
		Ip = thisIP;
		processID = processID2;
		this.rmiRecord = String.valueOf(processID);

	}

	public InetAddress getIp() {
		return Ip;
	}

	public void setIp(InetAddress ip) {
		Ip = ip;
	}

	public int getProcessID() {
		return processID;
	}

	public void setProcessID(int processID) {
		this.processID = processID;
	}

	public String getRmiRecord() {
		return rmiRecord;
	}

	public void setRmiRecord(String rmiRecord) {
		this.rmiRecord = rmiRecord;
	}

}
