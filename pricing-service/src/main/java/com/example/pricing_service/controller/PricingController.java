package com.example.pricing_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.pricing_service.dto.ServicePriceDTO;
import com.example.pricing_service.dto.ServicePriceUpdateDTO;
import com.example.pricing_service.dto.AddonPriceDTO;
import com.example.pricing_service.dto.AddonPriceUpdateDTO;
import com.example.pricing_service.dto.PriceCalculationRequest;
import com.example.pricing_service.dto.PriceCalculationResponse;
import com.example.pricing_service.entity.ServiceType;
import com.example.pricing_service.entity.AddOns;
import com.example.pricing_service.service.PricingService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Instant;

@RestController
@RequestMapping("/api/prices")
public class PricingController {
    private final PricingService pricingService;

    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    // Price calculation endpoint (used by the booking service)
    @PostMapping("/calculate")
    public ResponseEntity<PriceCalculationResponse> calculatePrice(@Valid @RequestBody PriceCalculationRequest request) {
        PriceCalculationResponse response = pricingService.calculatePrice(request);
        return ResponseEntity.ok(response);
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
            @RequestBody ServicePriceUpdateDTO servicePriceUpdateDTO) {
        return ResponseEntity.ok(pricingService.updateServicePrice(serviceType, servicePriceUpdateDTO));
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
            @RequestBody AddonPriceUpdateDTO addonPriceUpdateDTO) {
        return ResponseEntity.ok(pricingService.updateAddonPrice(addon, addonPriceUpdateDTO));
    }

    @DeleteMapping("/addons/{addon}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAddonPrice(@PathVariable AddOns addon) {
        pricingService.deleteAddonPrice(addon);
        return ResponseEntity.noContent().build();
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> healthStatus = new HashMap<>();
        
        // Basic service status
        healthStatus.put("status", "UP");
        healthStatus.put("timestamp", Instant.now().toString());
        
        // Service uptime
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        long uptime = runtimeMXBean.getUptime();
        healthStatus.put("uptime", uptime);
        
        // Memory information
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        Map<String, Long> memoryInfo = new HashMap<>();
        memoryInfo.put("total", totalMemory);
        memoryInfo.put("free", freeMemory);
        memoryInfo.put("used", usedMemory);
        healthStatus.put("memory", memoryInfo);
        
        return ResponseEntity.ok(healthStatus);
    }
}
