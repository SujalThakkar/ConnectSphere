package com.discretemath.connectsphere.ui;

import com.discretemath.connectsphere.database.DatabaseManager;
import com.discretemath.connectsphere.model.Recommendation;
import com.discretemath.connectsphere.model.User;
import com.discretemath.connectsphere.service.FriendService;
import com.discretemath.connectsphere.service.RecommendationService;
import com.discretemath.connectsphere.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardPanel extends JPanel {
    private final User currentUser;
    private final DatabaseManager db;
    private final FriendService friendService;
    private final RecommendationService recommendationService;
    private final UserService userService;

    private final DefaultListModel<String> friendsModel = new DefaultListModel<>();
    private final DefaultListModel<String> recModel = new DefaultListModel<>();
    private final DefaultListModel<String> allPeopleModel = new DefaultListModel<>();

    public DashboardPanel(JFrame parent, DatabaseManager db, User currentUser) {
        this.currentUser = currentUser;
        this.db = db;
        this.friendService = new FriendService(db);
        this.userService = new UserService(db);
        this.recommendationService = new RecommendationService(db, friendService);

        setLayout(new BorderLayout(18, 18));
        setBackground(UIStyle.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        // ---------------- HEADER ----------------
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIStyle.BACKGROUND);

        JLabel userLabel = new JLabel("Welcome, " + currentUser.getFullname());
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        userLabel.setForeground(UIStyle.PRIMARY_DARK);

        JButton logoutBtn = UIStyle.createButton("Logout");
        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightHeader.setBackground(UIStyle.BACKGROUND);
        rightHeader.add(logoutBtn);

        header.add(userLabel, BorderLayout.WEST);
        header.add(rightHeader, BorderLayout.EAST);

        // ---------------- LISTS ----------------
        JList<String> friendsList = new JList<>(friendsModel);
        friendsList.setFont(UIStyle.FONT_MAIN);

        JList<String> recList = new JList<>(recModel);
        recList.setFont(UIStyle.FONT_MAIN);

        JList<String> allPeopleList = new JList<>(allPeopleModel);
        allPeopleList.setFont(UIStyle.FONT_MAIN);
        allPeopleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // ---------- LEFT PANEL ----------
        JPanel left = UIStyle.createCardPanel("My Friends", new JScrollPane(friendsList));
        left.setPreferredSize(new Dimension(300, 420));

        // ---------- CENTER PANEL ----------
        JPanel center = UIStyle.createCardPanel("Recommended Connections", new JScrollPane(recList));
        center.setPreferredSize(new Dimension(340, 420));

        // ---------- RIGHT PANEL (All users + actions) ----------
        JPanel right = UIStyle.createCardPanel("All Users", new JScrollPane(allPeopleList));
        right.setPreferredSize(new Dimension(320, 420));

        // ---------- SEARCH + ACTIONS (redesigned) ----------
        JLabel searchLabel = new JLabel("Find User:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        searchLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField searchField = new JTextField();
        searchField.setFont(UIStyle.FONT_MAIN);
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35)); // full width

        JButton showReqsBtn = UIStyle.createButton("Requests");
        JButton connectBtn = UIStyle.createButton("Send Connect Request");
        JButton refreshBtn = UIStyle.createButton("Refresh");

        // buttons row (horizontal alignment)
        JPanel buttonsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonsRow.setBackground(UIStyle.BACKGROUND);
        buttonsRow.add(showReqsBtn);
        buttonsRow.add(connectBtn);
        buttonsRow.add(refreshBtn);

        // vertical layout for Find User section
        JPanel searchActions = new JPanel();
        searchActions.setLayout(new BoxLayout(searchActions, BoxLayout.Y_AXIS));
        searchActions.setBackground(UIStyle.BACKGROUND);
        searchActions.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchActions.add(searchLabel);
        searchActions.add(Box.createRigidArea(new Dimension(0, 5)));
        searchActions.add(searchField);
        searchActions.add(Box.createRigidArea(new Dimension(0, 12)));
        searchActions.add(buttonsRow);

        JPanel rightContainer = new JPanel(new BorderLayout(8, 8));
        rightContainer.setBackground(UIStyle.BACKGROUND);
        rightContainer.add(right, BorderLayout.CENTER);
        rightContainer.add(searchActions, BorderLayout.SOUTH);

        // ---------- LAYOUT ----------
        add(header, BorderLayout.NORTH);
        add(left, BorderLayout.WEST);
        add(center, BorderLayout.CENTER);
        add(rightContainer, BorderLayout.EAST);

        // ---------- ACTIONS ----------
        refreshBtn.addActionListener(e -> refreshAll());

        logoutBtn.addActionListener(e -> {
            parent.setContentPane(new LoginPanel(parent, db));
            parent.revalidate();
            parent.repaint();
        });

        connectBtn.addActionListener(e -> {
            String searched = searchField.getText().trim();
            String selected = allPeopleList.getSelectedValue();
            String targetUsername = (searched.isEmpty() ? selected : searched);
            if (targetUsername == null || targetUsername.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select or enter a username to connect");
                return;
            }

            User targetUser = userService.findByUsername(targetUsername);
            if (targetUser == null) {
                JOptionPane.showMessageDialog(this, "User not found");
                return;
            }
            if (targetUser.getId() == currentUser.getId()) {
                JOptionPane.showMessageDialog(this, "Cannot connect to yourself");
                return;
            }
            if (friendService.areFriends(currentUser.getId(), targetUser.getId())) {
                JOptionPane.showMessageDialog(this, "You are already friends with " + targetUsername);
                return;
            }

            boolean ok = friendService.sendRequest(currentUser.getId(), targetUser.getId());
            JOptionPane.showMessageDialog(this,
                    ok ? "Request sent to " + targetUsername : "Request failed / already sent");
            searchField.setText("");
            refreshAll();
        });

        showReqsBtn.addActionListener(e -> {
            JDialog d = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Pending Requests", true);
            d.setSize(420, 480);
            d.setLocationRelativeTo(this);
            d.setContentPane(new RequestsPanel(currentUser.getId(), db));
            d.setVisible(true);
            refreshAll();
        });

        // mutuals popup on recommendation double-click
        recList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    String val = recList.getSelectedValue();
                    if (val == null || val.startsWith("No")) return;

                    String target = val.split(" ")[0]; // assuming format "username (x mutuals)"
                    User targetUser = userService.findByUsername(target);
                    if (targetUser != null) {
                        List<String> mutuals = friendService.getMutualFriends(currentUser.getId(), targetUser.getId());
                        String msg = mutuals.isEmpty()
                                ? "No mutual connections with " + targetUser.getUsername()
                                : "Mutual connections with " + targetUser.getUsername() + ":\n" + String.join(", ", mutuals);
                        JOptionPane.showMessageDialog(DashboardPanel.this, msg,
                                "Mutual Connections", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        // double-click user to fill search
        allPeopleList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    String val = allPeopleList.getSelectedValue();
                    if (val != null) searchField.setText(val);
                }
            }
        });

        refreshAll();
    }

    private void refreshAll() {
        // friends
        friendsModel.clear();
        var friendIds = friendService.getFriendIds(currentUser.getId());
        for (Integer id : friendIds) {
            var u = userService.findById(id);
            if (u != null) friendsModel.addElement(u.getUsername());
        }

        // recommendations
        recModel.clear();
        List<Recommendation> recs = recommendationService.getRecommendations(currentUser.getId());
        if (recs.isEmpty()) recModel.addElement("No recommendations yet.");
        else recs.forEach(r -> recModel.addElement(r.toString()));

        // all users (exclude current + existing friends)
        allPeopleModel.clear();
        var all = userService.getAllUsers();
        var friendSet = friendIds.stream().collect(Collectors.toSet());
        for (User u : all) {
            if (u.getId() == currentUser.getId()) continue;
            if (friendSet.contains(u.getId())) continue;
            allPeopleModel.addElement(u.getUsername());
        }
    }
}
