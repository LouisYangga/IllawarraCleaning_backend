package com.example.user_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.dto.CreateUserDTO;
import com.example.user_service.dto.UpdateUserDTO;
import com.example.user_service.dto.UserDTO;

import com.example.user_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Instant;

@RestController
@RequestMapping("/api/users")
public class UserController {    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }   

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> healthStatus = new HashMap<>();
        
        // Basic service status
        healthStatus.put("status", "UP");
        healthStatus.put("timestamp", Instant.now().toString());
        
        // Service uptime
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        long uptime = runtimeMXBean.getUptime();
        healthStatus.put("uptime", uptime);
        
        // Memory information
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        Map<String, Long> memoryInfo = new HashMap<>();
        memoryInfo.put("total", totalMemory);
        memoryInfo.put("free", freeMemory);
        memoryInfo.put("used", usedMemory);
        healthStatus.put("memory", memoryInfo);
        
        return ResponseEntity.ok(healthStatus);
    }
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(404)
                .body(java.util.Collections.singletonMap("message", "No users found"));
        }
        return ResponseEntity.ok(users);
    }    
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<UserDTO> user = userService.getUserById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(404)
                .body(java.util.Collections.singletonMap("message", "User not found with ID: " + id));
        }
        return ResponseEntity.ok(user.get());
    
    }    
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        Optional<UserDTO> user = userService.getUserByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.status(404)
                .body(java.util.Collections.singletonMap("message", "User not found with email: " + email));
        }
        return ResponseEntity.ok(user.get());
    }
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserDTO createUserDto) {
        Optional<UserDTO> existingUser = userService.getUserByEmail(createUserDto.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(409)
                .body(java.util.Collections.singletonMap("message", "User with email already exists: " + createUserDto.getEmail()));
        }
        UserDTO createdUser = userService.createUser(createUserDto);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserDTO updateUserDto) {
        try {
            UserDTO updatedUser = userService.updateUserByEmail(updateUserDto.getEmail(), updateUserDto);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404)
                .body(java.util.Collections.singletonMap("message", "User not found with email: " + updateUserDto.getEmail()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok()
                .body(java.util.Collections.singletonMap("message", "User successfully deleted with ID: " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404)
                .body(java.util.Collections.singletonMap("message", "User not found with ID: " + id));
        }
    }
}
