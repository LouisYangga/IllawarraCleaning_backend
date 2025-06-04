package com.example.pricing_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pricing_service.entity.AddonPrice;
import com.example.pricing_service.entity.AddOns;
import java.util.Optional;
import java.util.Set;

public interface AddonPriceRepository extends JpaRepository<AddonPrice, Long> {
    Optional<AddonPrice> findByAddon(AddOns addon);
    Set<AddonPrice> findByAddonIn(Set<AddOns> addons);
    boolean existsByAddon(AddOns addon);
}
