package com.discretemath.connectsphere.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * UIStyle - central place for colors, fonts and styled components.
 */
public class UIStyle {

    public static final Color PRIMARY = new Color(40, 180, 170);
    public static final Color PRIMARY_DARK = new Color(25, 130, 125);
    public static final Color BACKGROUND = new Color(245, 247, 250);
    public static final Font FONT_MAIN = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 15);

    // Modern rounded button factory
    public static JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_MAIN);
        btn.setBackground(PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(PRIMARY_DARK);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(PRIMARY);
            }
        });

        return btn;
    }

    // Card-like panel with title at top
    public static JPanel createCardPanel(String title, JComponent content) {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_BOLD);
        titleLabel.setForeground(PRIMARY_DARK);
        titleLabel.setBorder(new EmptyBorder(0, 0, 8, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // place content inside a simple panel for padding
        JPanel contentWrap = new JPanel(new BorderLayout());
        contentWrap.setBackground(Color.WHITE);
        contentWrap.add(content, BorderLayout.CENTER);
        panel.add(contentWrap, BorderLayout.CENTER);

        return panel;
    }

    // Apply global background to frame
    public static void applyGlobalStyle(JFrame frame) {
        frame.getContentPane().setBackground(BACKGROUND);
        // setting a default font for look consistency (optional)
        frame.getContentPane().setFont(FONT_MAIN);
    }
}
