package com.discretemath.connectsphere.ui;

import com.discretemath.connectsphere.database.DatabaseManager;
import com.discretemath.connectsphere.model.FriendRequest;
import com.discretemath.connectsphere.service.FriendService;
import com.discretemath.connectsphere.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RequestsPanel extends JPanel {
    public RequestsPanel(int loggedUserId, DatabaseManager db) {
        setLayout(new BorderLayout(8,8));
        FriendService fs = new FriendService(db);
        UserService us = new UserService(db);

        DefaultListModel<FriendRequest> model = new DefaultListModel<>();
        JList<FriendRequest> list = new JList<>(model);
        list.setCellRenderer((l, value, index, isSelected, cellHasFocus) -> {
            String fromUsername = us.findById(value.getFromUser()).getUsername();
            JLabel lab = new JLabel(fromUsername + " wants to connect");
            lab.setOpaque(true);
            if (isSelected) lab.setBackground(Color.LIGHT_GRAY);
            return lab;
        });

        refresh(model, loggedUserId, db);

        JButton accept = new JButton("Accept");
        JButton reject = new JButton("Reject");

        UIUtils.styleButton(accept); UIUtils.styleButton(reject);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(reject); bottom.add(accept);

        add(new JScrollPane(list), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        accept.addActionListener(e -> {
            FriendRequest fr = list.getSelectedValue(); if (fr == null) return;
            boolean ok = fs.respondRequest(fr.getId(), true);
            if (!ok) JOptionPane.showMessageDialog(this, "Action failed");
            refresh(model, loggedUserId, db);
        });

        reject.addActionListener(e -> {
            FriendRequest fr = list.getSelectedValue(); if (fr == null) return;
            boolean ok = fs.respondRequest(fr.getId(), false);
            if (!ok) JOptionPane.showMessageDialog(this, "Action failed");
            refresh(model, loggedUserId, db);
        });
    }

    private void refresh(DefaultListModel<FriendRequest> model, int userId, DatabaseManager db) {
        model.clear();
        FriendService fs = new FriendService(db);
        List<FriendRequest> pending = fs.getPendingRequestsFor(userId);
        for (FriendRequest fr : pending) model.addElement(fr);
    }
}
