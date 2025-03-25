package fiap.restaurant.app.core.usecase.restaurant;

import fiap.restaurant.app.core.domain.Restaurant;
import fiap.restaurant.app.core.gateway.RestaurantGateway;

import java.util.List;
import java.util.UUID;

public class FindRestaurantsByOwnerIdUseCaseImpl implements FindRestaurantsByOwnerIdUseCase {

    private final RestaurantGateway restaurantGateway;

    public FindRestaurantsByOwnerIdUseCaseImpl(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    @Override
    public List<Restaurant> execute(UUID ownerId) {
        return restaurantGateway.findByOwnerId(ownerId);
    }
} 