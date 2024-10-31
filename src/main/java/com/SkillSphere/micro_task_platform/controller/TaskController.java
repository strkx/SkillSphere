package com.SkillSphere.micro_task_platform.controller;

import com.SkillSphere.micro_task_platform.entity.Skill;
import com.SkillSphere.micro_task_platform.entity.Task;
import com.SkillSphere.micro_task_platform.entity.User;
import com.SkillSphere.micro_task_platform.repository.SkillRepository;
import com.SkillSphere.micro_task_platform.repository.TaskRepository;
import com.SkillSphere.micro_task_platform.repository.UserRepository;
import com.SkillSphere.micro_task_platform.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private TaskService taskService;


    // Folder where files will be stored
    private final String FILE_STORAGE_LOCATION = "uploads/";

    // Create a new task with file upload option
    @PostMapping("/addTaskWithFile")
    public ResponseEntity<?> addTaskWithFile(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("status") String status,
            @RequestParam("difficulty") String difficulty,
            @RequestParam("file") MultipartFile file,
            @RequestParam("skillsRequired[0].skillName") List<String> skillNames,
            @RequestParam("skillsRequired[0].proficiencyLevel") List<String> proficiencyLevels
    ) {
        try {
            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setStatus(status);
            task.setDifficulty(difficulty);

            List<Skill> skillList = new ArrayList<>();
            for (int i = 0; i < skillNames.size(); i++) {
                String skillName = skillNames.get(i);
                String proficiencyLevel = proficiencyLevels.get(i);
                List<Skill> foundSkills = skillRepository.findBySkillName(skillName);
                if (foundSkills.isEmpty()) {
                    return ResponseEntity.badRequest().body("Skill not found: " + skillName);
                }
                Skill skill = foundSkills.get(0);
                skill.setProficiencyLevel(proficiencyLevel);
                skillList.add(skill);
            }
            task.setSkillsRequired(skillList);

            // Use an absolute path for the upload directory
            String uploadDir = "C:/uploads/";
            File uploadFolder = new File(uploadDir);

            // Create directory if it doesn't exist
            if (!uploadFolder.exists() && !uploadFolder.mkdirs()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Could not create upload directory.");
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File destFile = new File(uploadFolder, fileName);
            file.transferTo(destFile);

            task.setFileUrl(destFile.getAbsolutePath());

            Task savedTask = taskRepository.save(task);
            return ResponseEntity.ok(savedTask);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating task: " + e.getMessage());
        }
    }


    // Helper method to validate status
    private boolean isValidStatus(String status) {
        return Arrays.asList("pending", "completed", "in_progress").contains(status.toLowerCase());
    }

    // Helper method to validate difficulty
    private boolean isValidDifficulty(String difficulty) {
        return Arrays.asList("easy", "medium", "hard").contains(difficulty.toLowerCase());
    }


    // Implement file storage logic
    private String storeFile(MultipartFile file) {
        try {
            Path fileStorageLocation = Paths.get(FILE_STORAGE_LOCATION).toAbsolutePath().normalize();
            Files.createDirectories(fileStorageLocation);

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path targetLocation = fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return targetLocation.toString(); // Returning file path as URL
        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
    }

    // Get all tasks
    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return ResponseEntity.ok(tasks);
    }

    // Get a task by ID
    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable String taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            return ResponseEntity.ok(taskOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Update a task's information
    @PutMapping("/update/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable String taskId, @RequestBody Task taskDetails) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setTitle(taskDetails.getTitle());
            task.setDescription(taskDetails.getDescription());
            task.setStatus(taskDetails.getStatus());
            task.setSkillsRequired(taskDetails.getSkillsRequired());
            task.setFileUrl(taskDetails.getFileUrl()); // Update file URL
            return ResponseEntity.ok(taskRepository.save(task));
        }
        return ResponseEntity.notFound().build();
    }

    // Add skill to a task
    @PutMapping("/{taskId}/addSkill/{skillId}")
    public ResponseEntity<Task> addSkillToTask(@PathVariable String taskId, @PathVariable String skillId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        // Add the skill to the list if it's not already present
        if (!task.getSkillsRequired().contains(skill)) {
            task.getSkillsRequired().add(skill);
        }

        taskRepository.save(task);
        return ResponseEntity.ok(task);
    }

    //    // Endpoint to get tasks assigned to a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksByUserId(@PathVariable String userId) {
        Optional<User> user = userRepository.findByIdWithTasks(userId);
        if (user.isPresent() && !user.get().getTasksAssigned().isEmpty()) {
            return ResponseEntity.ok(user.get().getTasksAssigned());
        } else {
            return ResponseEntity.noContent().build();
        }
    }


    // Remove a skill from a task
    @PutMapping("/{taskId}/removeSkill/{skillId}")
    public ResponseEntity<Task> removeSkillFromTask(@PathVariable String taskId, @PathVariable String skillId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.getSkillsRequired().removeIf(skill -> skill.getId().equals(skillId));

        taskRepository.save(task);
        return ResponseEntity.ok(task);
    }

    // Delete a task by ID
    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable String taskId) {
        taskRepository.deleteById(taskId);
        return ResponseEntity.ok("Task with ID: " + taskId + " has been deleted!");
    }

    // Get tasks by a specific skill name (tasks that require a specific skill)
    @GetMapping("/skill/{skillName}")
    public List<Task> getTasksBySkill(@PathVariable String skillName) {
        return taskRepository.findBySkillsRequired_SkillName(skillName);
    }

    // Delete all tasks
    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllTasks() {
        taskRepository.deleteAll(); // Deletes all tasks from the repository
        return ResponseEntity.ok("All tasks have been deleted.");
    }

    // Mark task as completed and award points to the user
    @PutMapping("/{userId}/completeTask/{taskId}")
    public ResponseEntity<String> completeTask(@PathVariable String userId, @PathVariable String taskId) {
        // Find the task by id
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Find the user by id
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Award points based on difficulty
        int pointsAwarded = 0;
        switch (task.getDifficulty()) {
            case "hard":
                pointsAwarded = 15;
                break;
            case "medium":
                pointsAwarded = 10;
                break;
            case "easy":
                pointsAwarded = 5;
                break;
            default:
                pointsAwarded = 0;
                break;
        }

        // Update user's points and level
        user.addPoints(pointsAwarded);  // This will update both points and level
        user.updateLevel();

        // Save the updated user
        userRepository.save(user);

        // Mark the task as completed
        task.setStatus("completed");

        // Update the task within the user's assigned tasks
        user.getTasksAssigned().stream()
                .filter(t -> t.getId().equals(taskId))
                .forEach(t -> t.setStatus("completed"));

        // Save the task and user back to the database
        taskRepository.save(task);  // Save task to update in /api/tasks
        userRepository.save(user);  // Save user to update in /api/users

        return ResponseEntity.ok("Task completed, " + pointsAwarded + " points awarded, and level updated to " + user.getLevel());
    }

    @GetMapping("/{taskId}/file")
    public ResponseEntity<Resource> downloadFile(@PathVariable String taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        String fileUrl = task.getFileUrl();  // Get the file URL from the Task entity
        if (fileUrl == null || fileUrl.isEmpty()) {
            return ResponseEntity.notFound().build(); // If no file is attached
        }

        try {
            Path filePath = Paths.get(fileUrl);  // Get the file path
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                // Return the file as a downloadable resource
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error while reading file: " + e.getMessage());
        }
    }

    @PostMapping("/{taskId}/solution")
    public ResponseEntity<String> submitSolution(
            @PathVariable String taskId,
            @RequestParam("solutionText") String solutionText,
            @RequestParam(value = "solutionFile", required = false) MultipartFile solutionFile) {
        try {
            taskService.saveSolution(taskId, solutionText, solutionFile);
            return ResponseEntity.ok("Solution submitted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to submit solution: " + e.getMessage());
        }
    }
    @GetMapping("/{taskId}/solution-file")
    public ResponseEntity<Resource> downloadSolutionFile(@PathVariable String taskId) {
        try {
            // Fetch task details from the database to get the solution file URL
            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new RuntimeException("Task not found"));

            // Ensure solution file URL is set
            String solutionFilePath = task.getSolutionFileUrl();
            if (solutionFilePath == null || solutionFilePath.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Load the file as a resource
            Path filePath = Paths.get(solutionFilePath);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // Inside TaskController

    // Endpoint to get detailed information for a specific task
    @GetMapping("/{taskId}/details")
    public ResponseEntity<?> getTaskDetails(@PathVariable String taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            Map<String, Object> taskDetails = new HashMap<>();
            taskDetails.put("title", task.getTitle());
            taskDetails.put("description", task.getDescription());
            taskDetails.put("difficulty", task.getDifficulty());
            taskDetails.put("status", task.getStatus());

            if (task.getSolutionText() != null) {
                taskDetails.put("solutionText", task.getSolutionText());
                taskDetails.put("solutionFileUrl", task.getSolutionFileUrl());
            }

            return ResponseEntity.ok(taskDetails);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
    }


}
