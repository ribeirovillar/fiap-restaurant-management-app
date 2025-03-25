package fiap.restaurant.app.core.usecase.restaurant;

import fiap.restaurant.app.core.gateway.RestaurantGateway;

import java.util.UUID;

public class DeleteRestaurantUseCaseImpl implements DeleteRestaurantUseCase {

    private final RestaurantGateway restaurantGateway;

    public DeleteRestaurantUseCaseImpl(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    @Override
    public void execute(UUID id) {
        restaurantGateway.deleteById(id);
    }
} 