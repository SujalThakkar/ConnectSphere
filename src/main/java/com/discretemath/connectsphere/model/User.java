package com.discretemath.connectsphere.model;

public class User {
    private final int id;
    private final String username;
    private final String fullname;

    public User(int id, String username, String fullname) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getFullname() { return fullname; }
}
