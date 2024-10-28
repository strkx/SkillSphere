package com.SkillSphere.micro_task_platform.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@Document(collection = "tasks")
public class Task {

    @Id
    private String id;
    private String title;
    private String description;
    private String status;
    private String difficulty;
    private List<Skill> skillsRequired;
    private String fileUrl; // <-- Add this field to store the file path

    // Store userId as a String to associate a task with a user in MongoDB
    private String userId; // <-- This will be the reference to the user

    private String solutionText;


    public String getSolutionText() {
        return solutionText;
    }

    public void setSolutionText(String solutionText) {
        this.solutionText = solutionText;
    }

    public String getSolutionFileUrl() {
        return solutionFileUrl;
    }

    public void setSolutionFileUrl(String solutionFileUrl) {
        this.solutionFileUrl = solutionFileUrl;
    }

    private String solutionFileUrl;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<Skill> getSkillsRequired() {
        return skillsRequired;
    }

    public void setSkillsRequired(List<Skill> skillsRequired) {
        this.skillsRequired = skillsRequired;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", skillsRequired=" + skillsRequired +
                ", fileUrl='" + fileUrl + '\'' +
                ", userId='" + userId + '\'' + // Include userId in the toString method
                '}';
    }
}
