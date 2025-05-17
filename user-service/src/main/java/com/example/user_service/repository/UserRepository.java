package com.example.user_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.user_service.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    User findByPhoneNumber(Long phoneNumber);

    User findByFirstNameAndLastName(String firstName, String lastName);

    User findByAddress(String address);

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(Long phoneNumber);
}

