package com.discretemath.connectsphere;

import com.discretemath.connectsphere.database.DatabaseManager;
import com.discretemath.connectsphere.ui.LoginPanel;
import com.discretemath.connectsphere.ui.UIStyle;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatabaseManager db = new DatabaseManager();
            JFrame frame = new JFrame("ConnectSphere - Contact Recommendation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);

            UIStyle.applyGlobalStyle(frame);

            frame.setContentPane(new LoginPanel(frame, db));
            frame.setVisible(true);
        });
    }
}
