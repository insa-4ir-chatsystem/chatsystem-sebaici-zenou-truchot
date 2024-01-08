import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import network.UDPListener;
import views.LogIn;
import objects.SystemApp;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.println("Lancement de l'application");
        SystemApp app = SystemApp.getInstance();
        app.usersListUpdateRoutine();
        UDPListener listener = new UDPListener();
        listener.addObserver((message, address) -> System.out.println("Message reçu : " + message + " de " + address));

        listener.addObserver(app::receiveMessage);
        listener.start();

        LogIn logIn = new LogIn();
        logIn.create();

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                app.usersListUpdateRoutine();
            }
        }, 0, 60000);
    }
}
