package fiap.restaurant.app.core.usecase.restaurant;

import fiap.restaurant.app.core.domain.Restaurant;
import fiap.restaurant.app.core.exception.RestaurantNotFoundException;
import fiap.restaurant.app.core.gateway.RestaurantGateway;

import java.util.UUID;

public class FindRestaurantByIdUseCaseImpl implements FindRestaurantByIdUseCase {

    private final RestaurantGateway restaurantGateway;

    public FindRestaurantByIdUseCaseImpl(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    @Override
    public Restaurant execute(UUID id) {
        return restaurantGateway.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with id: " + id));
    }
} 