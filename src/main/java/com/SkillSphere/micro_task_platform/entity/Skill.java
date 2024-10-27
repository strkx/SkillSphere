package com.SkillSphere.micro_task_platform.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "skills")
public class Skill {

    @Id
    private String id;
    private String skillName;
    private String proficiencyLevel;

    // Constructors
    public Skill() {
    }

    public Skill(String id, String skillName, String proficiencyLevel) {
        this.id = id;
        this.skillName = skillName;
        this.proficiencyLevel = proficiencyLevel;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getProficiencyLevel() {
        return proficiencyLevel;
    }

    public void setProficiencyLevel(String proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }
}
