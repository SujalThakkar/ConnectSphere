package com.discretemath.connectsphere.ui;

import com.discretemath.connectsphere.database.DatabaseManager;
import com.discretemath.connectsphere.model.User;
import com.discretemath.connectsphere.service.UserService;

import javax.swing.*;
import java.awt.*;

/**
 * LoginPanel - modern login UI. On success switches to DashboardPanel.
 */
public class LoginPanel extends JPanel {
    private final JFrame parent;
    private final DatabaseManager db;
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public LoginPanel(JFrame parent, DatabaseManager db) {
        this.parent = parent;
        this.db = db;

        setLayout(new GridBagLayout());
        setBackground(UIStyle.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Welcome to ConnectSphere");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(UIStyle.PRIMARY_DARK);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        JLabel uLabel = new JLabel("Username:");
        uLabel.setFont(UIStyle.FONT_MAIN);
        gbc.gridx = 0;
        add(uLabel, gbc);

        usernameField = new JTextField(18);
        usernameField.setFont(UIStyle.FONT_MAIN);
        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel pLabel = new JLabel("Password:");
        pLabel.setFont(UIStyle.FONT_MAIN);
        add(pLabel, gbc);

        passwordField = new JPasswordField(18);
        passwordField.setFont(UIStyle.FONT_MAIN);
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridy++;
        gbc.gridx = 0; gbc.gridwidth = 2;
        JButton loginBtn = UIStyle.createButton("Login");
        add(loginBtn, gbc);

        gbc.gridy++;
        JButton registerBtn = UIStyle.createButton("Create Account");
        add(registerBtn, gbc);

        // services
        UserService userService = new UserService(db);

        // actions
        loginBtn.addActionListener(e -> {
            String u = usernameField.getText().trim();
            String p = new String(passwordField.getPassword()).trim();
            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter username and password");
                return;
            }

            User user = userService.login(u, p);
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid username/password");
                return;
            }

            // switch to dashboard (welcome will show user's fullname)
            DashboardPanel dash = new DashboardPanel(parent, db, user);
            parent.setContentPane(dash);
            parent.revalidate();
            parent.repaint();
        });

        registerBtn.addActionListener(e -> {
            RegisterPanel rp = new RegisterPanel(parent, db);
            parent.setContentPane(rp);
            parent.revalidate();
            parent.repaint();
        });
    }
}
