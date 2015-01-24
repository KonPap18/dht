import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;


public class AppNodeSignature implements Serializable {
	private InetAddress  Ip;
	private int processID;
	private String rmiRecord;//pvw ua eggrafei sto rmi
	public AppNodeSignature(InetAddress ip, int processID, String rmiRecord) {
		Ip = ip;
		processID = processID;
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
	public byte[] getBytes() throws IOException {
		// TODO Auto-generated method stub
		 Serializable sObj = (Serializable) this;
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ObjectOutputStream oos = new ObjectOutputStream(baos);
	        oos.writeObject(sObj);
	        byte[] bytes = baos.toByteArray();
	        return bytes;
	}
		
	

}
