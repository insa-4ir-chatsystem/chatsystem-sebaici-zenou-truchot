package objects;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import network.UDPSender;

public class SystemApp {
    private final User me;
    private final UserList myUserList;
    private final UDPSender udpSender;
    private static SystemApp instance = null;

    private SystemApp() throws SocketException, UnknownHostException {
        InetAddress address = getMyIp();

        this.me = new User("me", address);
        this.udpSender = new UDPSender(me.getIp());
        myUserList = new UserList(me);

    }

    public static SystemApp getInstance() throws SocketException, UnknownHostException {
        if (instance == null) {
            instance = new SystemApp();
        }
        return instance;
    }

    /**
     * Get the user of the system
     * @return the user
     */
    public User getMe() {
        return me;
    }

    /**
     * Get the userlist of the system
     * @return the userlist
     */
    public UserList getMyUserList(){
        return myUserList;
    }
    /**
     * Set the nickname of the user and return an error code to let the user know if the operation was successful or not.
     * @param nickname new nickname of the user
     */
    public int setMyUsername(String nickname){ // the success of the operation is returned as an int equal to 0 if success and 1 if the nickname is already taken and 2 if the nickname is not valid
        // First we need to check if the user is using a valid nickname
        if (nickname.equals("")) {
            return 2;
        }
        // Now that we know that the nickname us valid, we need to check if it is already taken
        if (myUserList.UserIsInListByNickmane(nickname)){
            return 1;
        }
        me.setNickname(nickname);
        myUserList.updateNickname(me.getIp(), me.getNickname());
        sendBroadcast("Nickname update : " + nickname);
        return 0;
    }

    /**
     * Set the nickname of a user of the userlist and return an error code to let the user know if the operation was successful or not.
     * @param address of the user to update
     * @param newNn new nickname of the user
     */
    public int setSomeoneUsername(InetAddress address, String newNn){ // the success of the operation is returned as an int equal to 0 if success and 1 if the user is not in the list and 2 if the nickname is already taken
        // if the user is not in the list, we add him
        if (!myUserList.UserIsInListByIp(address)){
            myUserList.addUser(new User(newNn, address));
            return 1;
        }
        // if the user is in the list but with a different nickname, we update his nickname if it is not already taken
        if(!myUserList.UserIsInListByNickmane(newNn)){
            myUserList.updateNickname(address, newNn);
            return 0;
        }
        // if the user is in the list with the same nickname, we do nothing
        System.out.println("Nickname already taken");
        return 2;
    }

    /**
     * Send a message to all users
     * @param message to send
     */
    public void sendBroadcast(String message) {
        try {
            udpSender.send(message, InetAddress.getByName("255.255.255.255"), true);
        } catch (IOException ignored) {
        }
    }

    /**
     * Send a message to a specific user
     * @param message to send
     * @param address of the user
     */
    public void sendUnicast(String message, InetAddress address) {
        try {
            udpSender.send(message, address, false);
        } catch (IOException ignored) {
        }
    }

    /**
     * Treatment of the received message
     * @param message received
     * @param address of the sender
     */
    public void receiveMessage(String message, InetAddress address) throws UnknownHostException {
        if (me.getIp().toString().contains(address.toString())) {
            return;
        }
        if (message.startsWith("update request")) {
            String messageToSend = "update response from : " + me.getNickname();
            sendUnicast(messageToSend, address);
        } else if (message.startsWith("update response from : ")) {
            // get the nickname of the user and add it to the list of users online if it is not already in it
            String nickname = message.substring(23);
            if (!myUserList.UserIsInListByIp(address)) {
                User user = new User(nickname, address);
                myUserList.addUser(user);
            } else {
                // if the user is already in the list, we update his nickname if it is different
                if (myUserList.getUserByIp(address).getNickname().equals(nickname)) {
                    return;
                }
                setSomeoneUsername(address, nickname);
            }
        } else if (message.startsWith("Nickname update : ")) {
            String nickname = message.substring(18);
            setSomeoneUsername(address, nickname);
        } else if (message.equals("disconnect")) {
            myUserList.updateUserStatus(address, 0);
        } else {
            // if the message is not a command, we display it
            System.out.println(message);
        }
    }

    /**
     * Send a broadcast message to all users to update the list of users online
     */
    public void usersListUpdateRoutine() {
        String updateMessage = "update request";
        sendBroadcast(updateMessage);
    }

    /**
     * Disconnect the user from the chat
     */
    public void disconnect() {
        sendBroadcast("disconnect");
        System.exit(0);
    }

    public InetAddress getMyIp() throws SocketException, UnknownHostException {

        String os = System.getProperty("os.name");
        InetAddress address = null;
        if (os.equals("Linux")) {
            System.out.println("Linux");
            Enumeration<NetworkInterface> nics = NetworkInterface
                    .getNetworkInterfaces();
            while (nics.hasMoreElements()) {
                NetworkInterface nic = nics.nextElement();
                Enumeration<InetAddress> addrs = nic.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress addr = addrs.nextElement();
                    if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                        System.out.println(addr.getHostAddress());
                        address = InetAddress.getByName(addr.getHostAddress());
                        break;
                    }
                }
            }
        } else if (os.startsWith("Windows")) {
            System.out.println("Windows");
            address = InetAddress.getLocalHost();
        } else {
            System.out.println("OS not supported");
        }

        return address;
    }

}