package com.example.pricing_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pricing_service.entity.ServicePrice;
import com.example.pricing_service.entity.ServiceType;
import java.util.Optional;

public interface ServicePriceRepository extends JpaRepository<ServicePrice, Long> {
    Optional<ServicePrice> findByServiceType(ServiceType serviceType);
    boolean existsByServiceType(ServiceType serviceType);
}
