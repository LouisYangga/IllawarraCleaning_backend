package com.example.booking_service.validator;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.booking_service.entity.AddOns;
import com.example.booking_service.entity.AddOnsUpdate;

@Component
public class AddOnsValidator {
    
    public void validateAddOnsUpdate(AddOnsUpdate addonsUpdate) {
        // Check if replace is used with add/remove
        if (addonsUpdate.getReplace() != null && 
            (addonsUpdate.getAdd() != null || addonsUpdate.getRemove() != null)) {
            throw new IllegalArgumentException(
                "Cannot use replace with add/remove operations simultaneously");
        }

        Set<String> validAddons = Arrays.stream(AddOns.values())
            .map(Enum::name)
            .collect(Collectors.toSet());

        // Validate each addon value
        if (addonsUpdate.getReplace() != null) {
            validateAddons(addonsUpdate.getReplace(), validAddons, "replace");
        }
        if (addonsUpdate.getAdd() != null) {
            validateAddons(addonsUpdate.getAdd(), validAddons, "add");
        }
        if (addonsUpdate.getRemove() != null) {
            validateAddons(addonsUpdate.getRemove(), validAddons, "remove");
        }
    }

    private void validateAddons(List<AddOns> addons, Set<String> validAddons, String operation) {
        List<String> invalidAddons = addons.stream()
            .map(Enum::name)
            .filter(addon -> !validAddons.contains(addon))
            .distinct()
            .collect(Collectors.toList());

        if (!invalidAddons.isEmpty()) {
            throw new IllegalArgumentException(
                String.format("Invalid addons in %s operation: %s. Valid addons are: %s",
                    operation,
                    String.join(", ", invalidAddons),
                    String.join(", ", validAddons))
            );
        }
    }
}