package com.discretemath.connectsphere.service;

import com.discretemath.connectsphere.database.DatabaseManager;
import com.discretemath.connectsphere.model.FriendRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendService {
    private final DatabaseManager db;

    public FriendService(DatabaseManager db) { this.db = db; }

    private int min(int a, int b) { return Math.min(a,b); }
    private int max(int a, int b) { return Math.max(a,b); }

    public boolean areFriends(int aId, int bId) {
        String sql = "SELECT 1 FROM friendships WHERE user1=? AND user2=?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, min(aId,bId));
            ps.setInt(2, max(aId,bId));
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) { return false; }
    }

    public boolean sendRequest(int fromId, int toId) {
        if (fromId == toId) return false;
        if (areFriends(fromId, toId)) return false;
        String check = "SELECT 1 FROM friend_requests WHERE from_user=? AND to_user=? AND status='PENDING'";
        try (PreparedStatement ps = db.getConnection().prepareStatement(check)) {
            ps.setInt(1, fromId); ps.setInt(2, toId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return false;
        } catch (SQLException e) { e.printStackTrace(); }

        String sql = "INSERT INTO friend_requests (from_user,to_user,status) VALUES (?, ?, 'PENDING')";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, fromId);
            ps.setInt(2, toId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<FriendRequest> getPendingRequestsFor(int userId) {
        List<FriendRequest> list = new ArrayList<>();
        String sql = "SELECT id, from_user, to_user, status FROM friend_requests WHERE to_user=? AND status='PENDING'";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(new FriendRequest(rs.getInt("id"), rs.getInt("from_user"), rs.getInt("to_user"), rs.getString("status")));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean respondRequest(int requestId, boolean accept) {
        try (PreparedStatement ps1 = db.getConnection().prepareStatement("SELECT from_user,to_user FROM friend_requests WHERE id=?")) {
            ps1.setInt(1, requestId);
            ResultSet rs = ps1.executeQuery();
            if (!rs.next()) return false;
            int f = rs.getInt("from_user"), t = rs.getInt("to_user");
            String status = accept ? "ACCEPTED" : "REJECTED";
            db.getConnection().setAutoCommit(false);
            try (PreparedStatement ps2 = db.getConnection().prepareStatement("UPDATE friend_requests SET status=? WHERE id=?")) {
                ps2.setString(1, status); ps2.setInt(2, requestId); ps2.executeUpdate();
            }
            if (accept) {
                try (PreparedStatement ps3 = db.getConnection().prepareStatement("INSERT OR IGNORE INTO friendships (user1, user2) VALUES (?, ?)")) {
                    ps3.setInt(1, min(f,t)); ps3.setInt(2, max(f,t)); ps3.executeUpdate();
                }
            }
            db.getConnection().commit();
            db.getConnection().setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try { db.getConnection().rollback(); db.getConnection().setAutoCommit(true); } catch (SQLException ignore) {}
            return false;
        }
    }

    public List<Integer> getFriendIds(int userId) {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT user1, user2 FROM friendships WHERE user1=? OR user2=?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId); ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int u1 = rs.getInt("user1"), u2 = rs.getInt("user2");
                ids.add(u1 == userId ? u2 : u1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ids;
    }

    // Add these imports at top of FriendService.java if not already present:
// import java.util.Set;
// import java.util.HashSet;
// import java.util.Collections;

public List<String> getMutualFriends(int userId, int otherId) {
    List<Integer> myFriends = getFriendIds(userId);
    List<Integer> otherFriends = getFriendIds(otherId);

    // intersection of friend id lists
    Set<Integer> common = new HashSet<>(myFriends);
    common.retainAll(otherFriends);

    if (common.isEmpty()) return Collections.emptyList();

    List<String> mutualUsernames = new ArrayList<>();
    String sql = "SELECT username FROM users WHERE id = ?";

    try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
        for (Integer id : common) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    mutualUsernames.add(rs.getString("username"));
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return mutualUsernames;
}

}


