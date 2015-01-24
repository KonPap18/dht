import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;


public class SignatureResponse implements Runnable {
	
	
	AppNodeSignature signatureToResponse;
	DatagramPacket signaturePacket;
	
	
	public SignatureResponse(AppNodeSignature signatureToResponse) {
		super();
		this.signatureToResponse = signatureToResponse;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		 MulticastSocket listeningSocket = null;
	     DatagramSocket responseSocket = null;
	     InetAddress multicastGroup;
	     signatureToResponse=null;
	     DatagramPacket reqPacket=null;
	     byte [] buffer=new byte[30];
	     
	     try {
	    	 listeningSocket=new MulticastSocket(4400);
	    	 listeningSocket.setReuseAddress(true);
	    	 responseSocket=new DatagramSocket();//fill later
	    	 multicastGroup=InetAddress.getByName(("230.0.1.5"));
	    	 listeningSocket.joinGroup(multicastGroup);
	    	 signaturePacket=new DatagramPacket(buffer, buffer.length);
	    	 reqPacket=new DatagramPacket(buffer, buffer.length);
	    	 
	     }catch(Exception e) {
	    	 
	    	 
	     }
	     
	     
	     while(!Thread.interrupted()) {
	    	 
	    	 try {
	    		 listeningSocket.receive(reqPacket);
	    		 buffer=signatureToResponse.getBytes();
	    		 signaturePacket=new DatagramPacket(buffer, buffer.length, reqPacket.getAddress(), 6000);
	    		 responseSocket.send(signaturePacket);
	    	 }catch(Exception e) {
	    		 
	    	 }
	     }
	     

	}

}
