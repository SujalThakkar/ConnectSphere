package com.discretemath.connectsphere.service;

import com.discretemath.connectsphere.database.DatabaseManager;
import com.discretemath.connectsphere.model.Recommendation;

import java.sql.*;
import java.util.*;

public class RecommendationService {
    private final DatabaseManager db;
    private final FriendService friendService;

    public RecommendationService(DatabaseManager db, FriendService friendService) {
        this.db = db;
        this.friendService = friendService;
    }

    // Graph traversal + set operations to produce recommendations
    public List<Recommendation> getRecommendations(int userId) {
        List<Integer> myFriends = friendService.getFriendIds(userId);
        Set<Integer> exclude = new HashSet<>(myFriends);
        exclude.add(userId);

        Map<Integer, Integer> mutualCount = new HashMap<>();
        for (Integer f : myFriends) {
            List<Integer> fof = friendService.getFriendIds(f);
            for (Integer candidate : fof) {
                if (!exclude.contains(candidate)) {
                    mutualCount.put(candidate, mutualCount.getOrDefault(candidate, 0) + 1);
                }
            }
        }

        List<Recommendation> recs = new ArrayList<>();
        try (PreparedStatement ps = db.getConnection().prepareStatement("SELECT username FROM users WHERE id=?")) {
            for (Map.Entry<Integer,Integer> e : mutualCount.entrySet()) {
                ps.setInt(1, e.getKey());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) recs.add(new Recommendation(rs.getString("username"), e.getValue()));
            }
        } catch (SQLException ex) { ex.printStackTrace(); }

        recs.sort((a,b) -> Integer.compare(b.getMutualCount(), a.getMutualCount()));
        return recs;
    }
}
