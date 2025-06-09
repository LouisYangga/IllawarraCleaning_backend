package com.example.pricing_service.mapper;

import org.springframework.stereotype.Component;
import com.example.pricing_service.dto.ServicePriceDTO;
import com.example.pricing_service.dto.ServicePriceUpdateDTO;
import com.example.pricing_service.entity.ServicePrice;

@Component
public class ServicePriceMapper {
    
    public ServicePriceDTO toDTO(ServicePrice entity) {
        if (entity == null) {
            return null;
        }
        
        ServicePriceDTO dto = new ServicePriceDTO();
        dto.setServiceType(entity.getServiceType());
        dto.setBasePrice(entity.getBasePrice());
        dto.setHourlyRate(entity.getHourlyRate());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public ServicePrice toEntity(ServicePriceDTO dto) {
        if (dto == null) {
            return null;
        }
        
        ServicePrice entity = new ServicePrice();
        entity.setServiceType(dto.getServiceType());
        entity.setBasePrice(dto.getBasePrice());
        entity.setHourlyRate(dto.getHourlyRate());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public void updateEntityFromDTO(ServicePriceUpdateDTO dto, ServicePrice entity) {
        if (dto == null || entity == null) {
            return;
        }
        
        if (dto.getServiceType() != null) {
            entity.setServiceType(dto.getServiceType());
        }        if (dto.getBasePrice() != null && dto.getBasePrice() > 0) {
            entity.setBasePrice(dto.getBasePrice());
        }
        if (dto.getHourlyRate() != null && dto.getHourlyRate() > 0) {
            entity.setHourlyRate(dto.getHourlyRate());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
    }
}
