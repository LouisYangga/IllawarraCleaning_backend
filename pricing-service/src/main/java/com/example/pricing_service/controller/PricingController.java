package com.example.pricing_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.pricing_service.dto.ServicePriceDTO;
import com.example.pricing_service.dto.AddonPriceDTO;
import com.example.pricing_service.dto.PriceCalculationRequest;
import com.example.pricing_service.entity.ServiceType;
import com.example.pricing_service.entity.AddOns;
import com.example.pricing_service.service.PricingService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/prices")
public class PricingController {

    private final PricingService pricingService;

    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    // Price calculation endpoint (used by the booking service)
    @PostMapping("/calculate")
    public ResponseEntity<Double> calculatePrice(@Valid @RequestBody PriceCalculationRequest request) {
        double price = pricingService.calculatePrice(request);
        return ResponseEntity.ok(price);
    }

    // Service Price endpoints
    @GetMapping("/services")
    public ResponseEntity<List<ServicePriceDTO>> getAllServicePrices() {
        return ResponseEntity.ok(pricingService.getAllServicePrices());
    }

    @GetMapping("/services/{serviceType}")
    public ResponseEntity<ServicePriceDTO> getServicePrice(@PathVariable ServiceType serviceType) {
        return ResponseEntity.ok(pricingService.getServicePrice(serviceType));
    }

    @PostMapping("/services")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServicePriceDTO> createServicePrice(@Valid @RequestBody ServicePriceDTO servicePriceDTO) {
        return ResponseEntity.ok(pricingService.createServicePrice(servicePriceDTO));
    }

    @PutMapping("/services/{serviceType}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServicePriceDTO> updateServicePrice(
            @PathVariable ServiceType serviceType,
            @Valid @RequestBody ServicePriceDTO servicePriceDTO) {
        return ResponseEntity.ok(pricingService.updateServicePrice(serviceType, servicePriceDTO));
    }

    @DeleteMapping("/services/{serviceType}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteServicePrice(@PathVariable ServiceType serviceType) {
        pricingService.deleteServicePrice(serviceType);
        return ResponseEntity.noContent().build();
    }

    // Addon Price endpoints
    @GetMapping("/addons")
    public ResponseEntity<List<AddonPriceDTO>> getAllAddonPrices() {
        return ResponseEntity.ok(pricingService.getAllAddonPrices());
    }

    @GetMapping("/addons/{addon}")
    public ResponseEntity<AddonPriceDTO> getAddonPrice(@PathVariable AddOns addon) {
        return ResponseEntity.ok(pricingService.getAddonPrice(addon));
    }

    @PostMapping("/addons")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AddonPriceDTO> createAddonPrice(@Valid @RequestBody AddonPriceDTO addonPriceDTO) {
        return ResponseEntity.ok(pricingService.createAddonPrice(addonPriceDTO));
    }

    @PutMapping("/addons/{addon}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AddonPriceDTO> updateAddonPrice(
            @PathVariable AddOns addon,
            @Valid @RequestBody AddonPriceDTO addonPriceDTO) {
        return ResponseEntity.ok(pricingService.updateAddonPrice(addon, addonPriceDTO));
    }

    @DeleteMapping("/addons/{addon}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAddonPrice(@PathVariable AddOns addon) {
        pricingService.deleteAddonPrice(addon);
        return ResponseEntity.noContent().build();
    }
}
