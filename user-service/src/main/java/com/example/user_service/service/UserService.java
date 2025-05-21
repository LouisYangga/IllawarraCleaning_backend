package com.example.user_service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.user_service.repository.UserRepository;
import com.example.user_service.entity.User;
import com.example.user_service.dto.UserDTO;
import com.example.user_service.dto.CreateUserDTO;
import com.example.user_service.dto.UpdateUserDTO;
import com.example.user_service.mapper.UserMapper;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    // Create
    @Transactional
    public UserDTO createUser(CreateUserDTO createUserDTO) {
        // Check if user already exists with email
        if (userRepository.existsByEmail(createUserDTO.getEmail())) {
            throw new RuntimeException("User already exists with email: " + createUserDTO.getEmail());
        }
        User user = userMapper.toEntity(createUserDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    // Read operations
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
            .map(userMapper::toDTO)
            .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
            .map(userMapper::toDTO);
    }

    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .map(userMapper::toDTO);
    }

    public UserDTO getUserByPhoneNumber(Long phoneNumber) {
        return userMapper.toDTO(userRepository.findByPhoneNumber(phoneNumber));
    }

    public UserDTO getUserByName(String firstName, String lastName) {
        return userMapper.toDTO(userRepository.findByFirstNameAndLastName(firstName, lastName));
    }
    
    // Update
    @Transactional
    public UserDTO updateUserByEmail(String email, UpdateUserDTO updateUserDTO) {
        return userRepository.findByEmail(email)
            .map(existingUser -> {
                userMapper.updateEntityFromDTO(updateUserDTO, existingUser);
                User savedUser = userRepository.save(existingUser);
                return userMapper.toDTO(savedUser);
            })
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    // Delete
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Check if user exists
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void incrementBookingCount(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.incrementBookingCount();
            userRepository.save(user);
        });
    }
}
