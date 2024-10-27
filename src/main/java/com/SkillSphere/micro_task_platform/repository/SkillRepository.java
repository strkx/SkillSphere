package com.SkillSphere.micro_task_platform.repository;

import com.SkillSphere.micro_task_platform.entity.Skill;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends MongoRepository<Skill, String> {
    // Add custom query methods if necessary
    List<Skill> findBySkillName(String skillName);

}
