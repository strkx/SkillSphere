package com.SkillSphere.micro_task_platform.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String username;
    private String email;
    private String password;

    // List to store the skills of the user
    private List<Skill> skills= new ArrayList<>();;

    // List to store the tasks assigned to the user
    private List<Task> tasksAssigned= new ArrayList<>();;

    private int points = 0;  // Track user points
    private int level;
    private String github;    // Optional
    private String linkedIn;  // Optional


    // Constructors
    public User() {
    }

    public User(String id, String username, String email, String password, List<Skill> skills, List<Task> tasksAssigned) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.skills = skills;
        this.tasksAssigned = tasksAssigned;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<Task> getTasksAssigned() {
        return tasksAssigned;
    }

    public void setTasksAssigned(List<Task> tasksAssigned) {
        this.tasksAssigned = tasksAssigned;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    public void updateLevel() {
        this.level = this.points / 100;  // Every 100 points increase level
    }

    public String getGithub() {
        return github;
    }
    public void setGithub(String github) {
        this.github = github;
    }
    public String getLinkedIn() {
        return linkedIn;
    }
    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }
}
