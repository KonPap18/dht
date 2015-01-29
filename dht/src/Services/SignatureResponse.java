package Services;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;

import DistributedStorageApp.AppNodeSignature;




public class SignatureResponse implements Runnable {
	
	
	AppNodeSignature signatureToResponse;
	DatagramPacket signaturePacket;
	
	
	public SignatureResponse(AppNodeSignature signatureToResponse) {
		
		this.signatureToResponse = signatureToResponse;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		 MulticastSocket listeningSocket = null;
	     DatagramSocket responseSocket = null;
	     InetAddress multicastGroup;
	     //signatureToResponse=null;
	     DatagramPacket reqPacket=null;
	     byte [] buffer=new byte[205];
	     
	     try {
	    	 listeningSocket=new MulticastSocket(4400);
	    	 listeningSocket.setReuseAddress(true);
	    	 responseSocket=new DatagramSocket();//fill later
	    	 multicastGroup=InetAddress.getByName(("230.0.1.5"));
	    	 listeningSocket.joinGroup(multicastGroup);
	    	 signaturePacket=new DatagramPacket(buffer, buffer.length);
	    	 reqPacket=new DatagramPacket(buffer, buffer.length);
	    	 
	     }catch(Exception e) {
	    	 System.out.println("!!Something went wrong in signature response setting!!");
	    	 
	     }
	     
	     
	     while(!Thread.interrupted()) {
	    	 
	    	 try {
	    		 listeningSocket.receive(reqPacket);
	    		 System.out.println("A new request came from " + reqPacket.getAddress().toString());
	    		 buffer=getBytes(signatureToResponse);
	    		// System.out.println("Answer send is: "+Arrays.toString(buffer));
	    		 signaturePacket=new DatagramPacket(buffer, buffer.length, reqPacket.getAddress(), 6000);
	    		 responseSocket.send(signaturePacket);
	    	 }catch(Exception e) {
		    	 System.out.println("!!-------Something went wrong in signature response thread-------!!");
		    	 e.printStackTrace(System.err);

	    	 }
	     }
	     

	}
	
	public static byte[] getBytes(Object obj) throws java.io.IOException
    {
        Serializable mySerializableObj = (Serializable) obj;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(mySerializableObj);
        byte[] bytes = baos.toByteArray();
       // System.out.println("Byte array length"+bytes.length);
        
        return bytes;

    }
}
