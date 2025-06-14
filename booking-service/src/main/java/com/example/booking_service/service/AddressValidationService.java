package com.example.booking_service.service;

import com.example.booking_service.dto.AddressValidationResult;
import com.example.booking_service.entity.Address;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AddressValidationService {
    private final GeoApiContext geoApiContext;
    private final LatLng ILLAWARRA_CENTER;
    private final double MAX_RADIUS_KM;

    public AddressValidationService(
            GeoApiContext geoApiContext,
            @Value("${app.location.center.lat:-34.4278}") double centerLat,
            @Value("${app.location.center.lng:150.8936}") double centerLng,
            @Value("${app.location.radius:100.0}") double maxRadiusKm) {
        this.geoApiContext = geoApiContext;
        this.ILLAWARRA_CENTER = new LatLng(centerLat, centerLng);
        this.MAX_RADIUS_KM = maxRadiusKm;
    }

    public AddressValidationResult validateAddress(Address address) {
        try {

            GeocodingResult[] results = GeocodingApi.geocode(geoApiContext, address.toString())
                    .await();

            if (results.length == 0) {
                log.warn("Address not found: {}", address.toString());
                return new AddressValidationResult(false, "Address not found", null, null);
            }

            GeocodingResult location = results[0];
            LatLng addressLocation = location.geometry.location;
            
            // Calculate distance from center
            double distance = calculateDistance(ILLAWARRA_CENTER, addressLocation);
            boolean isWithinRadius = distance <= MAX_RADIUS_KM;

            if (!isWithinRadius) {
                log.warn("Address outside service area: {} ({:.2f}km)", address.toString(), distance);
                return new AddressValidationResult(
                    false, 
                    String.format("Address is %.2f km outside our service area (max %d km)", 
                        distance - MAX_RADIUS_KM, (int)MAX_RADIUS_KM),
                    location.formattedAddress,
                    parseGoogleAddress(location)
                );
            }

            // Parse the validated address components
            Address validatedAddress = parseGoogleAddress(location);

            return new AddressValidationResult(
                true, 
                "Address validated successfully", 
                location.formattedAddress,
                validatedAddress
            );

        } catch (Exception e) {
            log.error("Error validating address: {}", e.getMessage());
            throw new RuntimeException("Address validation failed", e);
        }
    }

    private Address parseGoogleAddress(GeocodingResult result) {
        Address address = new Address();
        
        for (AddressComponent component : result.addressComponents) {
            for (AddressComponentType type : component.types) {
                switch (type) {
                    case STREET_NUMBER:
                        address.setStreet(component.longName + " " + 
                            getComponentByType(result, AddressComponentType.ROUTE));
                        break;
                    case LOCALITY:
                        address.setSuburb(component.longName);
                        break;
                    case ADMINISTRATIVE_AREA_LEVEL_1:
                        address.setState(component.shortName); // Uses state abbreviation
                        break;
                    case POSTAL_CODE:
                        address.setPostalCode(component.longName);
                        break;
                    default:
                        break;
                }
            }
        }
        return address;
    }

    private String getComponentByType(GeocodingResult result, AddressComponentType type) {
        for (AddressComponent component : result.addressComponents) {
            for (AddressComponentType componentType : component.types) {
                if (componentType == type) {
                    return component.longName;
                }
            }
        }
        return "";
    }

    private double calculateDistance(LatLng point1, LatLng point2) {
        final int EARTH_RADIUS_KM = 6371;
        double lat1 = Math.toRadians(point1.lat);
        double lat2 = Math.toRadians(point2.lat);
        double lng1 = Math.toRadians(point1.lng);
        double lng2 = Math.toRadians(point2.lng);

        double dLat = lat2 - lat1;
        double dLng = lng2 - lng1;

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dLng/2) * Math.sin(dLng/2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        
        return EARTH_RADIUS_KM * c;
    }
}
