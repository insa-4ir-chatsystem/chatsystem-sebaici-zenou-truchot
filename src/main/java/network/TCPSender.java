package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class TCPSender {
    
    private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public void startConnection(String ip, int port) throws IOException {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }

        public String sendMessage(String msg) throws IOException {
            out.println(msg);
            String resp = in.readLine();
            return resp;
        }

        public void stopConnection() throws IOException {
            in.close();
            out.close();
            clientSocket.close();
        }

        public static void main(String[] args) {
            String msg = args[0];
            TCPSender client = new TCPSender();
            try {
                client.startConnection("localhost", 6666);
                String response = client.sendMessage(msg);
                System.out.println(response);
                if (Objects.equals(msg, "close")) {
                    client.stopConnection();
                }
            }
            catch (IOException e) {
                    e.printStackTrace();
            }
        }
}