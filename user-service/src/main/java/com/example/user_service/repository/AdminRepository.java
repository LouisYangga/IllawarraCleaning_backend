package com.example.user_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.user_service.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    // Custom query methods can be defined here if needed
    // For example, you can add methods to find admins by specific criteria    Optional<Admin> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByLastLoginDate(java.time.LocalDate lastLoginDate);
}
