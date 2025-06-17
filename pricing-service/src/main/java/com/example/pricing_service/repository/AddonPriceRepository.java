package com.example.pricing_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pricing_service.entity.AddonPrice;
import com.example.pricing_service.entity.AddOns;

import java.util.List;
import java.util.Optional;

public interface AddonPriceRepository extends JpaRepository<AddonPrice, Long> {
    Optional<AddonPrice> findByAddon(AddOns addon);
    List<AddonPrice> findByAddonIn(List<AddOns> addons);
    boolean existsByAddon(AddOns addon);
}
