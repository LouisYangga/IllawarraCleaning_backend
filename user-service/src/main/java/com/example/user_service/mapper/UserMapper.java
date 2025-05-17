package com.example.user_service.mapper;

import org.springframework.stereotype.Component;
import com.example.user_service.dto.UserDTO;
import com.example.user_service.dto.CreateUserDTO;
import com.example.user_service.dto.UpdateUserDTO;
import com.example.user_service.entity.User;

@Component
public class UserMapper {
    
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        
        return new UserDTO(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getAddress(),
            user.getPhoneNumber(),
            user.getCreatedDate(),
            user.getUpdatedDate()
        );
    }
    
    public User toEntity(CreateUserDTO createUserDTO) {
        if (createUserDTO == null) {
            return null;
        }
        
        User user = new User();
        user.setFirstName(createUserDTO.getFirstName());
        user.setLastName(createUserDTO.getLastName());
        user.setEmail(createUserDTO.getEmail());
        user.setAddress(createUserDTO.getAddress());
        user.setPhoneNumber(createUserDTO.getPhoneNumber());
        return user;
    }
    
    public void updateEntityFromDTO(UpdateUserDTO updateUserDTO, User user) {
        if (updateUserDTO == null || user == null) {
            return;
        }
        
        if (updateUserDTO.getFirstName() != null) {
            user.setFirstName(updateUserDTO.getFirstName());
        }
        if (updateUserDTO.getLastName() != null) {
            user.setLastName(updateUserDTO.getLastName());
        }
        if (updateUserDTO.getAddress() != null) {
            user.setAddress(updateUserDTO.getAddress());
        }
        if (updateUserDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(updateUserDTO.getPhoneNumber());
        }
    }
}
