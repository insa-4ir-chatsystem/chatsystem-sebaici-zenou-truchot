package views;

import database.DbController;
import objects.SystemApp;
import objects.TCPMessage;
import objects.User;

import javax.swing.*;
import java.awt.*;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Chat {

    // Instance of the application system
    private final SystemApp app = SystemApp.getInstance();
    private final DbController dbController = DbController.getInstance();
    private final User receiver;
    private boolean historyOnly = false;

    public Chat(String receiverIP, boolean historyOnly) throws SocketException, UnknownHostException {
        this.receiver = app.getMyUserList().getUserByNickname(receiverIP);
        this.historyOnly = historyOnly;
    }

    public void create() {
        JFrame frame = new JFrame("Chat with " + receiver);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //set max size
        frame.setSize(1000, 1000);

        JPanel mainPanel = new JPanel();
        JPanel panelTop = new JPanel();
        JPanel panelMessageHistory = new JPanel();
        JPanel panelSendMessage = new JPanel();

        JLabel titleLabel = new JLabel("Chat with " + receiver);
        JButton topButton = new JButton("Stop chat");
        if (historyOnly) {
            topButton.setText("Start chat");
        }
        JTextField messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        if (historyOnly) {
            messageField.setEnabled(false);
            messageField.setText("You need to start a chat to send messages");
            sendButton.setEnabled(false);
        }

        // Set properties
        mainPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        panelTop.setLayout(new BorderLayout());
        panelMessageHistory.setLayout(new BoxLayout(panelMessageHistory, BoxLayout.Y_AXIS));
        panelSendMessage.setLayout(new BorderLayout());

        try {
            ArrayList<TCPMessage> messages = dbController.getMessagesOf(receiver.getIp());
            for (TCPMessage message : messages) {
                JPanel panelMessage = new JPanel();
                JLabel dateLabel = new JLabel(message.getDate());
                dateLabel.setFont(new Font("Arial", Font.ITALIC, 5));
                JLabel labelMessage = new JLabel(message.getSender() + " : " + message.getContent());
                panelMessage.add(dateLabel);
                panelMessage.add(labelMessage);
                if (message.getSender().toString().equals(app.getMe().getNickname())) {
                    panelMessage.setAlignmentX(Component.RIGHT_ALIGNMENT);
                } else {
                    panelMessage.setAlignmentX(Component.LEFT_ALIGNMENT);
                }
                panelMessageHistory.add(panelMessage);
            }
        } catch (SQLException | UnknownHostException e) {
            throw new RuntimeException(e);
        }

        // Events
        topButton.addActionListener(e -> {
            if (historyOnly) {
                // Start chat with the user
            } else {
                // Stop chat with the user
                JOptionPane.showConfirmDialog(null, "Are you sure you want to stop the chat with " + receiver + " ?", "Stop chat", JOptionPane.YES_NO_OPTION);
                if (JOptionPane.YES_OPTION == 0) {
                    frame.dispose();
                }
            }
        });

        //add components
        panelTop.add(titleLabel, BorderLayout.LINE_START);
        panelTop.add(topButton, BorderLayout.LINE_END);
        panelSendMessage.add(messageField, BorderLayout.CENTER);
        panelSendMessage.add(sendButton, BorderLayout.LINE_END);

        mainPanel.add(panelTop);
        mainPanel.add(panelMessageHistory);
        mainPanel.add(panelSendMessage);

        frame.add(mainPanel);
        frame.setVisible(true);

    }
}
