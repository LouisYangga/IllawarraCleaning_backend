package com.example.pricing_service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pricing_service.dto.ServicePriceDTO;
import com.example.pricing_service.dto.ServicePriceUpdateDTO;
import com.example.pricing_service.dto.AddonPriceDTO;
import com.example.pricing_service.dto.AddonPriceUpdateDTO;
import com.example.pricing_service.dto.PriceCalculationRequest;
import com.example.pricing_service.entity.ServicePrice;
import com.example.pricing_service.entity.AddonPrice;
import com.example.pricing_service.entity.ServiceType;
import com.example.pricing_service.entity.AddOns;
import com.example.pricing_service.mapper.ServicePriceMapper;
import com.example.pricing_service.mapper.AddonPriceMapper;
import com.example.pricing_service.repository.ServicePriceRepository;
import com.example.pricing_service.repository.AddonPriceRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PricingService {

    private final ServicePriceRepository servicePriceRepository;
    private final AddonPriceRepository addonPriceRepository;
    private final ServicePriceMapper servicePriceMapper;
    private final AddonPriceMapper addonPriceMapper;

    public PricingService(
            ServicePriceRepository servicePriceRepository,
            AddonPriceRepository addonPriceRepository,
            ServicePriceMapper servicePriceMapper,
            AddonPriceMapper addonPriceMapper) {
        this.servicePriceRepository = servicePriceRepository;
        this.addonPriceRepository = addonPriceRepository;
        this.servicePriceMapper = servicePriceMapper;
        this.addonPriceMapper = addonPriceMapper;
    }

    // Calculate total price for a service request
    public double calculatePrice(PriceCalculationRequest request) {
        ServicePrice servicePrice = servicePriceRepository.findByServiceType(request.getServiceType())
                .orElseThrow(() -> new IllegalArgumentException("Service type not found: " + request.getServiceType()));

        // Calculate base service price (base price + hourly rate * duration)
        double total = servicePrice.getBasePrice() + (servicePrice.getHourlyRate() * request.getDuration());

        // Add addon prices if any
        if (request.getAddons() != null && !request.getAddons().isEmpty()) {
            Set<AddonPrice> addonPrices = addonPriceRepository.findByAddonIn(request.getAddons());
            total += addonPrices.stream()
                    .mapToDouble(AddonPrice::getPrice)
                    .sum();
        }

        return total;
    }

    // Service Price CRUD operations
    public List<ServicePriceDTO> getAllServicePrices() {
        return servicePriceRepository.findAll().stream()
                .map(servicePriceMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ServicePriceDTO getServicePrice(ServiceType serviceType) {
        return servicePriceMapper.toDTO(
                servicePriceRepository.findByServiceType(serviceType)
                        .orElseThrow(() -> new IllegalArgumentException("Service price not found: " + serviceType))
        );
    }

    @Transactional
    public ServicePriceDTO createServicePrice(ServicePriceDTO servicePriceDTO) {
        if (servicePriceRepository.existsByServiceType(servicePriceDTO.getServiceType())) {
            throw new IllegalArgumentException("Service price already exists for: " + servicePriceDTO.getServiceType());
        }
        ServicePrice entity = servicePriceMapper.toEntity(servicePriceDTO);
        return servicePriceMapper.toDTO(servicePriceRepository.save(entity));
    }

    @Transactional
    public ServicePriceDTO updateServicePrice(ServiceType serviceType, ServicePriceUpdateDTO servicePriceUpdateDTO) {
        ServicePrice entity = servicePriceRepository.findByServiceType(serviceType)
                .orElseThrow(() -> new IllegalArgumentException("Service price not found: " + serviceType));
        servicePriceMapper.updateEntityFromDTO(servicePriceUpdateDTO, entity);
        return servicePriceMapper.toDTO(servicePriceRepository.save(entity));
    }

    @Transactional
    public void deleteServicePrice(ServiceType serviceType) {
        ServicePrice entity = servicePriceRepository.findByServiceType(serviceType)
                .orElseThrow(() -> new IllegalArgumentException("Service price not found: " + serviceType));
        servicePriceRepository.delete(entity);
    }

    // Addon Price CRUD operations
    public List<AddonPriceDTO> getAllAddonPrices() {
        return addonPriceRepository.findAll().stream()
                .map(addonPriceMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AddonPriceDTO getAddonPrice(AddOns addon) {
        return addonPriceMapper.toDTO(
                addonPriceRepository.findByAddon(addon)
                        .orElseThrow(() -> new IllegalArgumentException("Addon price not found: " + addon))
        );
    }

    @Transactional
    public AddonPriceDTO createAddonPrice(AddonPriceDTO addonPriceDTO) {
        if (addonPriceRepository.existsByAddon(addonPriceDTO.getAddon())) {
            throw new IllegalArgumentException("Addon price already exists for: " + addonPriceDTO.getAddon());
        }
        AddonPrice entity = addonPriceMapper.toEntity(addonPriceDTO);
        return addonPriceMapper.toDTO(addonPriceRepository.save(entity));
    }

    @Transactional
    public AddonPriceDTO updateAddonPrice(AddOns addon, AddonPriceUpdateDTO addonPriceUpdateDTO) {
        AddonPrice entity = addonPriceRepository.findByAddon(addon)
                .orElseThrow(() -> new IllegalArgumentException("Addon price not found: " + addon));
        addonPriceMapper.updateEntityFromDTO(addonPriceUpdateDTO, entity);
        return addonPriceMapper.toDTO(addonPriceRepository.save(entity));
    }

    @Transactional
    public void deleteAddonPrice(AddOns addon) {
        AddonPrice entity = addonPriceRepository.findByAddon(addon)
                .orElseThrow(() -> new IllegalArgumentException("Addon price not found: " + addon));
        addonPriceRepository.delete(entity);
    }
}
