package com.SkillSphere.micro_task_platform.controller;

import com.SkillSphere.micro_task_platform.dto.UserLoginRequest;
import com.SkillSphere.micro_task_platform.entity.User;
import com.SkillSphere.micro_task_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000") // Allow requests from your React frontend
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // User Registration Endpoint (Sign-Up)
    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody User userDetails) {
        // Check if username or email is already in use
        if (userRepository.existsByUsername(userDetails.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username is already in use"));
        }
        if (userRepository.existsByEmail(userDetails.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is already in use"));
        }

        // Hash the password
        String hashedPassword = passwordEncoder.encode(userDetails.getPassword());
        userDetails.setPassword(hashedPassword);

        // Save the new user
        User savedUser = userRepository.save(userDetails);

        // Return user ID in the response
        return ResponseEntity.ok(Map.of("message", "User registered successfully", "userId", savedUser.getId()));
    }


    // User Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());
        if (userOptional.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), userOptional.get().getPassword())) {
            User user = userOptional.get();
            // Include the userId in the response
            Map<String, Object> response = new HashMap<>();
            response.put("userId", user.getId()); // Ensure getId() returns the user ID
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

}
