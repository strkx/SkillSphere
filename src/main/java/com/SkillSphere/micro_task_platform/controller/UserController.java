package com.SkillSphere.micro_task_platform.controller;

import com.SkillSphere.micro_task_platform.dto.LeaderboardDTO;
import com.SkillSphere.micro_task_platform.entity.Skill;
import com.SkillSphere.micro_task_platform.entity.Task;
import com.SkillSphere.micro_task_platform.entity.User;
import com.SkillSphere.micro_task_platform.repository.SkillRepository;
import com.SkillSphere.micro_task_platform.repository.TaskRepository;
import com.SkillSphere.micro_task_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private TaskRepository taskRepository;

    // Get all users
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Add a new user
    @PostMapping("/add")
    public User addUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // Update a user's information
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User userDetails) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            user.setSkills(userDetails.getSkills());
            user.setTasksAssigned(userDetails.getTasksAssigned());
            User updatedUser = userRepository.save(user);
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build(); // Return 404 if user not found
    }

    // Delete a user by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("User with ID: " + id + " has been deleted!");
        }
        return ResponseEntity.notFound().build(); // Return 404 if user not found
    }

    // Add skill to user
    @PutMapping("/{userId}/addSkill/{skillId}")
    public ResponseEntity<User> addSkillToUser(@PathVariable String userId, @PathVariable String skillId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        // Ensure the skill isn't already added
        if (!user.getSkills().contains(skill)) {
            user.getSkills().add(skill);
            userRepository.save(user);
        }

        return ResponseEntity.ok(user);
    }

    // Remove a skill from a user
    @PutMapping("/{userId}/removeSkill/{skillId}")
    public ResponseEntity<User> removeSkillFromUser(@PathVariable String userId, @PathVariable String skillId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean removed = user.getSkills().removeIf(skill -> skill.getId().equals(skillId));
        if (removed) {
            userRepository.save(user);
        }

        return ResponseEntity.ok(user);
    }

    // Get users by skill name
    @GetMapping("/skill/{skillName}")
    public List<User> getUsersBySkill(@PathVariable String skillName) {
        return userRepository.findBySkills_SkillName(skillName);
    }

    // Get suggested tasks for a user based on their skills
    @GetMapping("/{userId}/suggestedTasks")
    public ResponseEntity<List<Task>> getSuggestedTasks(@PathVariable String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Skill> userSkills = user.getSkills();
        if (userSkills.isEmpty()) {
            return ResponseEntity.ok(new ArrayList<>()); // Return empty list if no skills
        }

        // Find tasks that match any of the user's skills
        List<Task> suggestedTasks = new ArrayList<>();
        for (Skill skill : userSkills) {
            List<Task> matchingTasks = taskRepository.findBySkillsRequired_SkillName(skill.getSkillName());
            suggestedTasks.addAll(matchingTasks);
        }

        // Remove duplicate tasks if the user has multiple skills that match the same task
        List<Task> uniqueSuggestedTasks = suggestedTasks.stream()
                .distinct()
                .collect(Collectors.toList());

        return ResponseEntity.ok(uniqueSuggestedTasks);
    }
    @PutMapping("/{userId}/assignTask/{taskId}")
    public ResponseEntity<User> assignTaskToUser(@PathVariable String userId, @PathVariable String taskId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        user.getTasksAssigned().add(task);  // Assuming the User has a list of assigned tasks
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }


    @PutMapping("/{userId}/completeTask/{taskId}")
    public ResponseEntity<String> completeTask(@PathVariable String userId, @PathVariable String taskId) {
        // Find the task by id
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Find the user by id
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update the task status to "completed"
        task.setStatus("completed");

        // Update the task within the user's assigned tasks
        user.getTasksAssigned().stream()
                .filter(t -> t.getId().equals(taskId))
                .forEach(t -> t.setStatus("completed"));

        // Save the task and user back to the database
        taskRepository.save(task);  // Save task to update in /api/tasks
        userRepository.save(user);  // Save user to update in /api/users

        return ResponseEntity.ok("Task completed and updated for both task and user.");
    }
    @GetMapping("/leaderboard")
    public List<LeaderboardDTO> getLeaderboard() {
        Sort sortByPointsDesc = Sort.by(Sort.Direction.DESC, "points");
        List<User> topUsers = userRepository.findAll(sortByPointsDesc);

        // Map the top users to LeaderboardDTO and return only username and points
        return topUsers.stream()
                .limit(10) // Limit to top 10
                .map(user -> new LeaderboardDTO(user.getUsername(), user.getPoints()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserProfile(@PathVariable String userId) {
        return userRepository.findById(userId)
                .map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }

    // Optionally implement other methods here like get a user by ID, etc.
}
