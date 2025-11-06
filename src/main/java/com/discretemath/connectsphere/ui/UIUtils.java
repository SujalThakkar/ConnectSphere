package com.discretemath.connectsphere.ui;

import javax.swing.*;
import java.awt.*;

public class UIUtils {
    public static void styleButton(JButton b) {
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
        b.setFont(b.getFont().deriveFont(Font.BOLD, 13f));
    }

    public static JPanel titledPanel(String title, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(title),
                BorderFactory.createEmptyBorder(8,8,8,8)));
        p.add(comp, BorderLayout.CENTER);
        return p;
    }
}
