package fiap.restaurant.app.core.domain;

import fiap.restaurant.app.core.exception.UnauthorizedOperationException;
import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Restaurant {
    private UUID id;
    private String name;
    private Address address;
    private CuisineType cuisineType;
    private List<BusinessHours> businessHours;
    private User owner;
    private String createdAt;
    private String updatedAt;
    
    private static final int MIN_NAME_LENGTH = 3;
    
    public void setName(String name) {
        if (name != null && name.trim().length() < MIN_NAME_LENGTH) {
            throw new IllegalArgumentException("Restaurant name must be at least " + MIN_NAME_LENGTH + " characters long");
        }
        this.name = name;
    }
    
    public void setOwner(User owner) {
        if (owner != null && owner.getUserType() != null && 
            !UserType.OWNER.equals(owner.getUserType().getName())) {
            throw new UnauthorizedOperationException("Restaurant owner must have OWNER type");
        }
        this.owner = owner;
    }
    
    public void setBusinessHours(List<BusinessHours> businessHours) {
        if (businessHours != null) {
            for (BusinessHours hour : businessHours) {
                validateBusinessHours(hour);
            }
        }
        this.businessHours = businessHours;
    }
    
    private void validateBusinessHours(BusinessHours businessHours) {
        if (businessHours.getDayOfWeek() == null) {
            throw new IllegalArgumentException("Day of week is required for business hours");
        }
        
        if (!businessHours.isClosed()) {
            if (businessHours.getOpeningTime() == null) {
                throw new IllegalArgumentException("Opening time is required when business is not closed");
            }
            if (businessHours.getClosingTime() == null) {
                throw new IllegalArgumentException("Closing time is required when business is not closed");
            }
            if (businessHours.getClosingTime().isBefore(businessHours.getOpeningTime())) {
                throw new IllegalArgumentException("Closing time cannot be before opening time");
            }
        }
    }
    
    public void validateForCreation() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant name is required");
        }
        
        if (name.trim().length() < MIN_NAME_LENGTH) {
            throw new IllegalArgumentException("Restaurant name must be at least " + MIN_NAME_LENGTH + " characters long");
        }
        
        if (cuisineType == null) {
            throw new IllegalArgumentException("Cuisine type is required");
        }
        
        if (owner == null) {
            throw new IllegalArgumentException("Owner is required");
        }
        
        if (owner.getUserType() == null || !UserType.OWNER.equals(owner.getUserType().getName())) {
            throw new UnauthorizedOperationException("Only OWNER users can create restaurants");
        }
        
        if (businessHours == null || businessHours.isEmpty()) {
            throw new IllegalArgumentException("Business hours are required");
        }
        
        for (BusinessHours hour : businessHours) {
            validateBusinessHours(hour);
        }
        
        if (address != null) {
            address.validateForCreation();
        }
    }
    
    public void validateForUpdate(Restaurant existingRestaurant) {
        if (name != null && name.trim().length() < MIN_NAME_LENGTH) {
            throw new IllegalArgumentException("Restaurant name must be at least " + MIN_NAME_LENGTH + " characters long");
        }
        
        if (owner != null) {
            if (owner.getUserType() == null || !UserType.OWNER.equals(owner.getUserType().getName())) {
                throw new UnauthorizedOperationException("Only OWNER users can update restaurants");
            }
            
            if (existingRestaurant.getOwner() != null && 
                !owner.getId().equals(existingRestaurant.getOwner().getId())) {
                throw new UnauthorizedOperationException("You cannot change the owner of a restaurant");
            }
        }
        
        if (businessHours != null && !businessHours.isEmpty()) {
            for (BusinessHours hour : businessHours) {
                validateBusinessHours(hour);
            }
        }
        
        if (address != null) {
            address.validateForUpdate();
        }
    }
    
    public void validateForDelete() {
        if (owner == null) {
            throw new IllegalArgumentException("Owner is required for deletion");
        }
        
        if (owner.getUserType() == null || !UserType.OWNER.equals(owner.getUserType().getName())) {
            throw new UnauthorizedOperationException("Only OWNER users can delete restaurants");
        }
    }
} 