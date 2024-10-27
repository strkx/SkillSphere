package com.SkillSphere.micro_task_platform.dto;

public class LeaderboardDTO {
    private String username;
    private int points;

    // Constructor
    public LeaderboardDTO(String username, int points) {
        this.username = username;
        this.points = points;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
