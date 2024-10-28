// src/main/java/com/SkillSphere/micro_task_platform/service/TaskService.java

package com.SkillSphere.micro_task_platform.service;

import com.SkillSphere.micro_task_platform.entity.Task;
import com.SkillSphere.micro_task_platform.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void saveSolution(String taskId, String solutionText, MultipartFile solutionFile) throws IOException {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty()) {
            throw new RuntimeException("Task not found");
        }

        Task task = taskOptional.get();
        task.setSolutionText(solutionText);

        // Absolute path for the solutions directory
        String uploadDir = "C:/uploads/solutions/";
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists() && !uploadFolder.mkdirs()) {
            throw new RuntimeException("Could not create upload directory: " + uploadDir);
        }

        // Save solution file
        if (solutionFile != null && !solutionFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + solutionFile.getOriginalFilename();
            File destinationFile = new File(uploadFolder, fileName);
            solutionFile.transferTo(destinationFile);

            task.setSolutionFileUrl(destinationFile.getAbsolutePath());  // Save the absolute file path
        }

        task.setStatus("submitted");
        taskRepository.save(task);
    }

}
