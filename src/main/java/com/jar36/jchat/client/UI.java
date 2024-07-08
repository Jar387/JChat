package com.jar36.jchat.client;

import javax.swing.*;

public class UI {
    public static LoginInterface loginInterface;
    public static MainInterface mainInterface;

    public static void startLoginInterface() {
        loginInterface = new LoginInterface();
        loginInterface.setSize(300, 400);
        loginInterface.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loginInterface.setTitle(ClientMain.about);
        loginInterface.setVisible(true);
    }

    public static void startMainInterface() {
        loginInterface.setVisible(false);
        mainInterface = new MainInterface();
        mainInterface.setSize(800, 600);
        mainInterface.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainInterface.setTitle(ClientMain.about);
        mainInterface.setVisible(true);
    }

    public static void error(String msg) {
        JOptionPane.showConfirmDialog(null, msg, "Error", JOptionPane.DEFAULT_OPTION);
    }
}
