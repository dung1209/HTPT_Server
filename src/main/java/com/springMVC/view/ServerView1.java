package com.springMVC.view;

import javax.swing.*;

public class ServerView1 extends JFrame{
    private JTextField tfClientID;
    private JTextField tfClientReq;
    private JPanel contentPanel;

    public ServerView1() {
        setContentPane(contentPanel);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void setClientID(String clientID) {
        if (tfClientID != null) {
            tfClientID.setText(clientID);
        }
    }

    public void setClientRequest(String request) {
        if (tfClientReq != null) {
            tfClientReq.setText(request);
        }
    }
}
