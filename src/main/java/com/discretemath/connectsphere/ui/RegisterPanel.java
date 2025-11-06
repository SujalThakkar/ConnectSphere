package com.discretemath.connectsphere.ui;

import com.discretemath.connectsphere.database.DatabaseManager;
import com.discretemath.connectsphere.service.UserService;

import javax.swing.*;
import java.awt.*;

/**
 * RegisterPanel - user registration UI. On success navigates back to LoginPanel.
 */
public class RegisterPanel extends JPanel {
    private final JFrame parent;
    private final DatabaseManager db;

    public RegisterPanel(JFrame parent, DatabaseManager db) {
        this.parent = parent;
        this.db = db;

        setLayout(new GridBagLayout());
        setBackground(UIStyle.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Create New Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(UIStyle.PRIMARY_DARK);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        JLabel nameLabel = new JLabel("Full name:");
        nameLabel.setFont(UIStyle.FONT_MAIN);
        gbc.gridx = 0;
        add(nameLabel, gbc);
        JTextField nameField = new JTextField(18);
        nameField.setFont(UIStyle.FONT_MAIN);
        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(UIStyle.FONT_MAIN);
        add(usernameLabel, gbc);
        JTextField usernameField = new JTextField(18);
        usernameField.setFont(UIStyle.FONT_MAIN);
        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(UIStyle.FONT_MAIN);
        add(passLabel, gbc);
        JPasswordField passwordField = new JPasswordField(18);
        passwordField.setFont(UIStyle.FONT_MAIN);
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridy++;
        gbc.gridx = 0; gbc.gridwidth = 2;
        JButton registerBtn = UIStyle.createButton("Register");
        add(registerBtn, gbc);

        gbc.gridy++;
        JButton backBtn = UIStyle.createButton("Back to Login");
        add(backBtn, gbc);

        UserService userService = new UserService(db);

        registerBtn.addActionListener(e -> {
            String fullname = nameField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            if (fullname.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }
            boolean ok = userService.register(username, fullname, password);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Registration successful. Please login.");
                parent.setContentPane(new LoginPanel(parent, db));
                parent.revalidate();
                parent.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists or error occurred.");
            }
        });

        backBtn.addActionListener(e -> {
            parent.setContentPane(new LoginPanel(parent, db));
            parent.revalidate();
            parent.repaint();
        });
    }
}
