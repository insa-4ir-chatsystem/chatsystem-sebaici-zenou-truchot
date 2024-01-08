package views;

import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import objects.SystemApp;

public class LogIn {

    SystemApp app = SystemApp.getInstance();

    public LogIn() throws SocketException, UnknownHostException {
    }

    public void create(){

        JFrame frame = new JFrame("LogIn");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);

        JPanel panel = new JPanel();
        JPanel panelNickname = new JPanel();
        JLabel label = new JLabel("Enter your nickname");
        JTextField tf = new JTextField(10);
        JPanel panelbutton = new JPanel();
        JButton send = new JButton("Send");
        panelNickname.add(label);
        panelNickname.add(tf);
        panelNickname.add(send);
        panelbutton.add(send);

        panel.add(panelNickname);
        panel.add(panelbutton);
        panel.setAlignmentY(JPanel.CENTER_ALIGNMENT);
        frame.add(panel);

        send.addActionListener(e -> {
            app.usersListUpdateRoutine();
            String nickname = tf.getText();
            if (nickname.equals("")) {
                JOptionPane.showMessageDialog(frame, "Please enter a nickname");
                return;
            }
            int status = app.setMyUsername(nickname);
            if(status == 0){
                frame.setVisible(false);
                frame.dispose();
                Chat chat;
                try {
                    chat = new Chat();
                } catch (SocketException | UnknownHostException ex) {
                    throw new RuntimeException(ex);
                }
                chat.create();
            } else {
                JOptionPane.showMessageDialog(frame, "error");
            }
        });

        frame.setVisible(true);
    }

}
