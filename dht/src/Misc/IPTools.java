package Misc;


import java.net.MulticastSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.io.IOException;

public class IPTools
{

    public static InetAddress getIP() throws IOException
    {
        MulticastSocket socket = new MulticastSocket(5000);//multicast socket
        socket.setReuseAddress(true);
        InetAddress group = InetAddress.getByName("225.0.1.1");
        socket.joinGroup(group);
        byte buff[] = new byte[8];
        DatagramPacket loopback = new DatagramPacket(buff, buff.length, group, 5000);

        /*Multicast and receive same packet*/
        socket.send(loopback);

        socket.receive(loopback);
        
        socket.leaveGroup(group);
        socket.disconnect();
        return loopback.getAddress();

    }
}
