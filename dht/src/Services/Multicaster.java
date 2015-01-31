package Services;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Arrays;

import DistributedStorageApp.AppNodeSignature;




public class Multicaster {

	public static AppNodeSignature findTheLuckyOne() throws IOException {
		// TODO Auto-generated method stub
		 /*Stream declaration*/
        DataInputStream socketStream;

        /*Multicast a request to specific group (Nodes)*/
        MulticastSocket querySocket = null;
        DatagramSocket listeningSocket = null;
        InetAddress group = null;

        /*Dummy Request*/
        byte buff[] = new byte[300];//changed tjis it was 562


        /*Outcoming and incoming packet declaration*/
        DatagramPacket answer = null;
        DatagramPacket dummyPacket = null;
        try
        {

            /*Bind the sockets*/
            listeningSocket = new DatagramSocket(6000);
            listeningSocket.setReuseAddress(true);

            querySocket = new MulticastSocket(4400);
            querySocket.setLoopbackMode(false);
            querySocket.setTimeToLive(10);//--------------
            
            group = InetAddress.getByName("230.0.1.5");

            querySocket.joinGroup(group);
            System.out.println("joined multicast group");
            answer = new DatagramPacket(buff, buff.length);
            dummyPacket = new DatagramPacket(buff, buff.length, group, 4400);

            

        } catch (Exception ioe)
        {
            if (ioe instanceof IOException)
            {
                System.out.println("Error joining Multicast group.");
            } else if (ioe instanceof java.net.UnknownHostException)
            {
                System.out.println("Uknown host.");
            } else
            {
                System.out.println(ioe.getMessage());
            }
        }

        querySocket.setLoopbackMode(false);

        /*Send a packet to multicast group members*/
      // Log.addMessage("Sending multicast from client for bootstraping", Log.INFORMATION);
        
        querySocket.send(dummyPacket);

        /*Set timeout for server to act accordingly*/
        listeningSocket.setSoTimeout(3000);
        
        try
        {
            listeningSocket.receive(answer);
            
         
            /*Get the bytes of the udp packet and deserialize it by custom method*/
            AppNodeSignature LuckySignature = (AppNodeSignature)toObject(answer.getData());
           // Log.addMessage("Found a bootstrap node: " + properties.getPid(), Log.INFORMATION);
            listeningSocket.close();
            querySocket.close();
            if(!(LuckySignature==null)) {
            	System.out.println("Multicaster says: "+LuckySignature.getProcessID()+" answered");
            }
            return LuckySignature;
        } catch (java.net.SocketTimeoutException ste)
        {
            
            System.out.println("A bootstrap node was not found.");
            listeningSocket.close();
            querySocket.close();
            return null;
        }
        catch(Exception general)
        {
            System.err.println("Some exc:" + general.getMessage());
        }



        return null;

    }

	 private static Object toObject(byte[] bytes) throws IOException, ClassNotFoundException
	    {
		// 	System.out.println("received Answer in bytes is: "+Arrays.toString(bytes));
	       java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(bytes);
	        java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bais);
	        java.io.Serializable obj = (Serializable)ois.readObject();
	        return obj;
	    }
	 private static AppNodeSignature toAppNodeSignature(byte[] bytes) throws IOException, ClassNotFoundException
	    {
	       java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(bytes);
	        java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bais);
	        java.io.Serializable obj = (Serializable)ois.readObject();
	        return (AppNodeSignature) obj;
	    }
}
	


