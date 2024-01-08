package network;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


public class UDPListener extends Thread{

    public interface Observer {
        void handle(String message , InetAddress address) throws UnknownHostException;
    }

    public void addObserver(Observer observer) {
        synchronized (this.observers) {
            this.observers.add(observer);
        }
    }

    static int port = 49000;
    private final DatagramSocket socket;

    private final List<Observer> observers = new ArrayList<>();

    public UDPListener() throws SocketException {
        this.socket = new DatagramSocket(port);
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());

                synchronized (this.observers) {
                    for (Observer observer : observers) {
                        observer.handle(message, packet.getAddress());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
