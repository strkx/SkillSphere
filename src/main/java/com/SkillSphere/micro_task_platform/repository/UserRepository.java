package com.SkillSphere.micro_task_platform.repository;
import com.SkillSphere.micro_task_platform.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // Custom query method to find users by skill name
    List<User> findBySkills_SkillName(String skillName);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);  // Add this method
    @Query("{ '_id': ?0 }")
    Optional<User> findByIdWithTasks(String userId);

}
