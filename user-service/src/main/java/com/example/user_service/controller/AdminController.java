package com.example.user_service.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_service.dto.AdminDTO;
import com.example.user_service.dto.CreateAdminDTO;
import com.example.user_service.service.AdminService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

@RestController
@RequestMapping("/api/admin")
@Validated
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    
    @PostMapping("/create")
    public ResponseEntity<?> createAdmin(@Valid @RequestBody CreateAdminDTO createAdminDTO) {
        try {
            if (adminService.checkAdminExists(createAdminDTO.getEmail())) {
                return ResponseEntity.status(409)
                    .body(java.util.Collections.singletonMap("message", "Admin with email already exists: " + createAdminDTO.getEmail()));
            }
            AdminDTO createdAdmin = adminService.createAdmin(createAdminDTO);
            return ResponseEntity.ok(createdAdmin);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(java.util.Collections.singletonMap("message", e.getMessage()));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@Valid @RequestBody CreateAdminDTO createAdminDTO) {
        try {
            boolean isAuthenticated = adminService.adminLogin(createAdminDTO.getEmail(), createAdminDTO.getPassword());
            if (!isAuthenticated) {
                return ResponseEntity.status(401)
                    .body(java.util.Collections.singletonMap("message", "Invalid email or password"));
            }
            return ResponseEntity.ok(java.util.Collections.singletonMap("message", "Login successful"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(java.util.Collections.singletonMap("message", e.getMessage()));
        }
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getAdminByEmail(@PathVariable @Email String email) {
        try {
            AdminDTO admin = adminService.getAdminByEmail(email);
            if (admin == null) {
                return ResponseEntity.status(404)
                    .body(java.util.Collections.singletonMap("message", "Admin not found with email: " + email));
            }
            return ResponseEntity.ok(admin);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(java.util.Collections.singletonMap("message", e.getMessage()));
        }
    }
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<?> deleteAdminByEmail(
            @PathVariable @Email String email, 
            @Valid @RequestBody CreateAdminDTO authorisedAdmin) {
        try {
            String response = adminService.deleteAdminByEmail(email, authorisedAdmin);
            return ResponseEntity.ok(java.util.Collections.singletonMap("message", response));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                .body(java.util.Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(java.util.Collections.singletonMap("message", e.getMessage()));
        }
    }
}
