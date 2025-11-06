package com.discretemath.connectsphere.model;

public class Recommendation {
    private final String username;
    private final int mutualCount;

    public Recommendation(String username, int mutualCount) {
        this.username = username;
        this.mutualCount = mutualCount;
    }

    public String getUsername() { return username; }
    public int getMutualCount() { return mutualCount; }

    @Override
    public String toString() {
        return username + " (" + mutualCount + " mutual)";
    }
}
