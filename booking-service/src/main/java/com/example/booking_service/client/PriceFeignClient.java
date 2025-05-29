package com.example.booking_service.client;

import com.example.booking_service.entity.ServiceType;
import com.example.booking_service.entity.AddOns;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@FeignClient(name = "price-service", url = "${price.service.url}")
public interface PriceFeignClient {

    @PostMapping("/api/prices/calculate")
    double calculatePrice(
        @RequestParam ServiceType serviceType,
        @RequestParam Set<AddOns> addons,
        @RequestParam double duration
    );
}
