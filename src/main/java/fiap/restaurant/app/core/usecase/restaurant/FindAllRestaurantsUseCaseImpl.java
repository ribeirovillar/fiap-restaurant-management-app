package fiap.restaurant.app.core.usecase.restaurant;

import fiap.restaurant.app.core.domain.Restaurant;
import fiap.restaurant.app.core.gateway.RestaurantGateway;

import java.util.List;

public class FindAllRestaurantsUseCaseImpl implements FindAllRestaurantsUseCase {

    private final RestaurantGateway restaurantGateway;

    public FindAllRestaurantsUseCaseImpl(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    @Override
    public List<Restaurant> execute() {
        return restaurantGateway.findAll();
    }
} 