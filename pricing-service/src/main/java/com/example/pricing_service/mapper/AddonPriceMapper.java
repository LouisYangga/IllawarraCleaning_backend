package com.example.pricing_service.mapper;

import org.springframework.stereotype.Component;
import com.example.pricing_service.dto.AddonPriceDTO;
import com.example.pricing_service.dto.AddonPriceUpdateDTO;
import com.example.pricing_service.entity.AddonPrice;

@Component
public class AddonPriceMapper {
    
    public AddonPriceDTO toDTO(AddonPrice entity) {
        if (entity == null) {
            return null;
        }
        
        return new AddonPriceDTO(
            entity.getAddon(),
            entity.getPrice(),
            entity.getDescription()
        );
    }

    public AddonPrice toEntity(AddonPriceDTO dto) {
        if (dto == null) {
            return null;
        }
        
        AddonPrice entity = new AddonPrice();
        entity.setAddon(dto.getAddon());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public void updateEntityFromDTO(AddonPriceUpdateDTO dto, AddonPrice entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getAddon() != null) {
            entity.setAddon(dto.getAddon());
        }
        if (dto.getPrice() != null && dto.getPrice() > 0) {
            entity.setPrice(dto.getPrice());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
    }
}
