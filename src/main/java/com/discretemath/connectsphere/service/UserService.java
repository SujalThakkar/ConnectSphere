package com.discretemath.connectsphere.service;

import com.discretemath.connectsphere.database.DatabaseManager;
import com.discretemath.connectsphere.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final DatabaseManager db;

    public UserService(DatabaseManager db) { this.db = db; }

    public boolean register(String username, String fullname, String password) {
        String sql = "INSERT INTO users (username, fullname, password) VALUES (?, ?, ?)";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, fullname);
            ps.setString(3, password);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public User login(String username, String password) {
        String sql = "SELECT id, username, fullname FROM users WHERE username=? AND password=?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new User(rs.getInt("id"), rs.getString("username"), rs.getString("fullname"));
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public User findByUsername(String username) {
        String sql = "SELECT id, username, fullname FROM users WHERE username=?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new User(rs.getInt("id"), rs.getString("username"), rs.getString("fullname"));
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public User findById(int id) {
        String sql = "SELECT id, username, fullname FROM users WHERE id=?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new User(rs.getInt("id"), rs.getString("username"), rs.getString("fullname"));
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // NEW: return list of all users (useful to display "All people" list)
    public List<User> getAllUsers() {
        List<User> out = new ArrayList<>();
        String sql = "SELECT id, username, fullname FROM users ORDER BY username COLLATE NOCASE";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.add(new User(rs.getInt("id"), rs.getString("username"), rs.getString("fullname")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return out;
    }
}
