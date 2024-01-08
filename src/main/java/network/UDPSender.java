package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPSender {

    private static DatagramSocket socket = null;

    public UDPSender( InetAddress adresse) throws SocketException {
        //default sender port
        int myPort = 49001;
        socket = new DatagramSocket(myPort, adresse);
    }
    
    public void send( String message, InetAddress address, boolean broadcast) throws IOException {
        socket = new DatagramSocket();
        socket.setBroadcast(broadcast);
        byte[] buffer = message.getBytes();
        //default receiver port
        int otherPort = 49000;
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, otherPort);
        socket.send(packet);
        socket.close();
    }

}

