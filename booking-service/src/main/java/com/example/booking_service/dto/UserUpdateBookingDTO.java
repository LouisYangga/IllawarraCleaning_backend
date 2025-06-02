package com.example.booking_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.example.booking_service.entity.AddOnsUpdate;
import com.example.booking_service.entity.Address;
import com.example.booking_service.entity.ServiceType;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@NoArgsConstructor
public class UserUpdateBookingDTO implements AddonsUpdateAware {
    
// Make all fields optional
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String firstName;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastName;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long phoneNumber;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ServiceType serviceType;
    // For addons, use a special wrapper class to handle partial updates
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AddOnsUpdate addons;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String notes;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Address address;
}
