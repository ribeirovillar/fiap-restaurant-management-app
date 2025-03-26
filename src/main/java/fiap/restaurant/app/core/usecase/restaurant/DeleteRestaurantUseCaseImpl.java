package fiap.restaurant.app.core.usecase.restaurant;

import fiap.restaurant.app.core.domain.Restaurant;
import fiap.restaurant.app.core.gateway.RestaurantGateway;

import java.util.UUID;

public class DeleteRestaurantUseCaseImpl implements DeleteRestaurantUseCase {

    private final RestaurantGateway restaurantGateway;
    private final FindRestaurantByIdUseCase findRestaurantByIdUseCase;

    public DeleteRestaurantUseCaseImpl(RestaurantGateway restaurantGateway, FindRestaurantByIdUseCase findRestaurantByIdUseCase) {
        this.restaurantGateway = restaurantGateway;
        this.findRestaurantByIdUseCase = findRestaurantByIdUseCase;
    }

    @Override
    public void execute(UUID id) {
        Restaurant restaurant = findRestaurantByIdUseCase.execute(id);
        restaurant.validateForDelete();
        restaurantGateway.deleteById(id);
    }
} 