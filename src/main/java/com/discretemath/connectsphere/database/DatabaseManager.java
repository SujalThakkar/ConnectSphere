package com.discretemath.connectsphere.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:connectsphere.db";
    private Connection conn;

    public DatabaseManager() {
        try {
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("[DB] connected to " + DB_URL);
            initSchema();
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
        }
    }

    private void initSchema() {
        try (Statement stmt = conn.createStatement()) {
            
            // Users table
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    fullname TEXT NOT NULL,
                    password TEXT NOT NULL
                );
                """;

            // Friendships table
            String createFriendshipsTable = """
                CREATE TABLE IF NOT EXISTS friendships (
                    user1 INTEGER NOT NULL,
                    user2 INTEGER NOT NULL,
                    PRIMARY KEY (user1, user2),
                    FOREIGN KEY (user1) REFERENCES users(id),
                    FOREIGN KEY (user2) REFERENCES users(id)
                );
                """;

            // Friend requests table
            String createRequestsTable = """
                CREATE TABLE IF NOT EXISTS friend_requests (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    from_user INTEGER NOT NULL,
                    to_user INTEGER NOT NULL,
                    status TEXT NOT NULL,
                    FOREIGN KEY (from_user) REFERENCES users(id),
                    FOREIGN KEY (to_user) REFERENCES users(id)
                );
                """;

            stmt.execute(createUsersTable);
            stmt.execute(createFriendshipsTable);
            stmt.execute(createRequestsTable);

            System.out.println("[DB] tables initialized ✅");

        } catch (SQLException e) {
            System.out.println("❌ Schema initialization failed: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return conn;
    }
}
