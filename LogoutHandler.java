package com.mycompany.oodj_assignment;

import javax.swing.*;

public class LogoutHandler {
    public static void logout(JFrame frame, LogoutCallback logoutCallback) {
        int option = JOptionPane.showConfirmDialog(frame, "Do you want to Log Out?", "Log Out", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            frame.setVisible(false);
            logoutCallback.onLogout();
        }
    }
}

