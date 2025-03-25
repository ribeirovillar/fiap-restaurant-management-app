package fiap.restaurant.app.core.usecase.restaurant;

import fiap.restaurant.app.core.domain.Restaurant;
import fiap.restaurant.app.core.domain.User;
import fiap.restaurant.app.core.exception.UnauthorizedOperationException;

import java.util.UUID;

public class ValidateRestaurantOwnerUseCase {
    
    private final FindRestaurantByIdUseCase findRestaurantByIdUseCase;
    
    public ValidateRestaurantOwnerUseCase(FindRestaurantByIdUseCase findRestaurantByIdUseCase) {
        this.findRestaurantByIdUseCase = findRestaurantByIdUseCase;
    }
    
    public Restaurant execute(UUID restaurantId, User user) {
        Restaurant restaurant = findRestaurantByIdUseCase.execute(restaurantId);
        
        if (!user.getId().equals(restaurant.getOwner().getId())) {
            throw new UnauthorizedOperationException("You are not authorized to modify this restaurant");
        }
        
        return restaurant;
    }
    
    public void validateForCreation(User owner) {
        // The validation is handled in the Restaurant domain
        // We only need to ensure the owner exists
        if (owner == null) {
            throw new IllegalArgumentException("Owner is required");
        }
    }
    
    public void validateForUpdate(User ownerInRequest, User existingOwner) {
        // The validation for owner type is handled in the Restaurant domain
        // We only need to check owner ID match
        if (ownerInRequest == null || existingOwner == null) {
            throw new IllegalArgumentException("Owner information is required");
        }
        
        if (!ownerInRequest.getId().equals(existingOwner.getId())) {
            throw new UnauthorizedOperationException("You cannot change the owner of a restaurant");
        }
    }
    
    public void validateForDelete(User owner) {
        // The validation is handled in the Restaurant domain
        // We only need to ensure the owner exists
        if (owner == null) {
            throw new IllegalArgumentException("Owner is required");
        }
    }
} 