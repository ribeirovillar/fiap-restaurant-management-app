package fiap.restaurant.app.core.usecase.restaurant;

import fiap.restaurant.app.core.domain.Restaurant;
import fiap.restaurant.app.core.gateway.RestaurantGateway;

import java.util.UUID;

public class UpdateRestaurantUseCaseImpl implements UpdateRestaurantUseCase {

    private final RestaurantGateway restaurantGateway;
    private final FindRestaurantByIdUseCase findRestaurantByIdUseCase;

    public UpdateRestaurantUseCaseImpl(RestaurantGateway restaurantGateway, FindRestaurantByIdUseCase findRestaurantByIdUseCase) {
        this.restaurantGateway = restaurantGateway;
        this.findRestaurantByIdUseCase = findRestaurantByIdUseCase;
    }

    @Override
    public Restaurant execute(UUID id, Restaurant restaurant) {
        findRestaurantByIdUseCase.execute(id);
        restaurant.setId(id);
        restaurant.validateForUpdate(restaurant);
        return restaurantGateway.update(restaurant);
    }
} 