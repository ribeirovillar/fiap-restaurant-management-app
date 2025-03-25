package fiap.restaurant.app.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Address {
    private UUID id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    
    private static final int MIN_STREET_LENGTH = 3;
    private static final int MIN_CITY_LENGTH = 2;
    private static final int MIN_STATE_LENGTH = 2;
    private static final int MIN_ZIP_LENGTH = 5;
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public void setStreet(String street) {
        if (street != null && street.trim().length() < MIN_STREET_LENGTH) {
            throw new IllegalArgumentException("Street must be at least " + MIN_STREET_LENGTH + " characters long");
        }
        this.street = street;
    }
    
    public void setCity(String city) {
        if (city != null && city.trim().length() < MIN_CITY_LENGTH) {
            throw new IllegalArgumentException("City must be at least " + MIN_CITY_LENGTH + " characters long");
        }
        this.city = city;
    }
    
    public void setState(String state) {
        if (state != null && state.trim().length() < MIN_STATE_LENGTH) {
            throw new IllegalArgumentException("State must be at least " + MIN_STATE_LENGTH + " characters long");
        }
        this.state = state;
    }
    
    public void setZipCode(String zipCode) {
        if (zipCode != null && zipCode.trim().length() < MIN_ZIP_LENGTH) {
            throw new IllegalArgumentException("Zip code must be at least " + MIN_ZIP_LENGTH + " characters long");
        }
        this.zipCode = zipCode;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public void validateForCreation() {
        if (street == null || street.trim().isEmpty()) {
            throw new IllegalArgumentException("Street is required");
        }
        
        if (street.trim().length() < MIN_STREET_LENGTH) {
            throw new IllegalArgumentException("Street must be at least " + MIN_STREET_LENGTH + " characters long");
        }
        
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City is required");
        }
        
        if (city.trim().length() < MIN_CITY_LENGTH) {
            throw new IllegalArgumentException("City must be at least " + MIN_CITY_LENGTH + " characters long");
        }
        
        if (state == null || state.trim().isEmpty()) {
            throw new IllegalArgumentException("State is required");
        }
        
        if (state.trim().length() < MIN_STATE_LENGTH) {
            throw new IllegalArgumentException("State must be at least " + MIN_STATE_LENGTH + " characters long");
        }
        
        if (zipCode == null || zipCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Zip code is required");
        }
        
        if (zipCode.trim().length() < MIN_ZIP_LENGTH) {
            throw new IllegalArgumentException("Zip code must be at least " + MIN_ZIP_LENGTH + " characters long");
        }
    }
    
    public void validateForUpdate() {
        if (street != null && street.trim().length() < MIN_STREET_LENGTH) {
            throw new IllegalArgumentException("Street must be at least " + MIN_STREET_LENGTH + " characters long");
        }
        
        if (city != null && city.trim().length() < MIN_CITY_LENGTH) {
            throw new IllegalArgumentException("City must be at least " + MIN_CITY_LENGTH + " characters long");
        }
        
        if (state != null && state.trim().length() < MIN_STATE_LENGTH) {
            throw new IllegalArgumentException("State must be at least " + MIN_STATE_LENGTH + " characters long");
        }
        
        if (zipCode != null && zipCode.trim().length() < MIN_ZIP_LENGTH) {
            throw new IllegalArgumentException("Zip code must be at least " + MIN_ZIP_LENGTH + " characters long");
        }
    }
}
