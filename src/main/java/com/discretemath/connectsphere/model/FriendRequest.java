package com.discretemath.connectsphere.model;

public class FriendRequest {
    private final int id;
    private final int fromUser;
    private final int toUser;
    private final String status; // PENDING / ACCEPTED / REJECTED

    public FriendRequest(int id, int fromUser, int toUser, String status) {
        this.id = id;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.status = status;
    }

    public int getId() { return id; }
    public int getFromUser() { return fromUser; }
    public int getToUser() { return toUser; }
    public String getStatus() { return status; }
}
