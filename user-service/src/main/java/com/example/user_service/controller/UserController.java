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

@RestController
@RequestMapping("/api/users")
public class UserController {    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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

    @PutMapping
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
