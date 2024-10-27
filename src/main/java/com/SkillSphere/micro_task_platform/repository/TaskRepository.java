package com.SkillSphere.micro_task_platform.repository;

import com.SkillSphere.micro_task_platform.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findBySkillsRequired_SkillName(String skillName);
    List<Task> findByUserId(String userId);
    List<Task> findByStatus(String status);
}


