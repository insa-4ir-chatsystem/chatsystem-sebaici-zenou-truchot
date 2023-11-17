import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import UDP.UDPListener;
import UDP.UDPSender;
import Vues.LogIn;
import objects.SystemApp;

public class Main {
    public static void main(String[] args) throws IOException {

        LogIn.create();
        SystemApp app = new SystemApp();
        app.start();

        /*
        switch (args[0]){
            case "s":
                UDPListener listener = new UDPListener();
                listener.start();
                break;
            case "c":
                int port = 4889;
                UDPSender sender = new UDPSender (InetAddress.getLocalHost(), port);
                String msg = "Connection from "+ InetAddress.getLocalHost() + " via le port " + port;
                sender.broadcast(msg, InetAddress.getByName("255.255.255.255"));
                break;
        }
        */
    }

}
