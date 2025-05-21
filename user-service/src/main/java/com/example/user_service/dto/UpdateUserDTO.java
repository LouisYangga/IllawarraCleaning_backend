package com.example.user_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Long phoneNumber;
}
