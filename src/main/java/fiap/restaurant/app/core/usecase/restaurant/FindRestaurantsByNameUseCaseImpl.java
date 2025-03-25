package fiap.restaurant.app.core.usecase.restaurant;

import fiap.restaurant.app.core.domain.Restaurant;
import fiap.restaurant.app.core.gateway.RestaurantGateway;

import java.util.List;

public class FindRestaurantsByNameUseCaseImpl implements FindRestaurantsByNameUseCase {

    private final RestaurantGateway restaurantGateway;

    public FindRestaurantsByNameUseCaseImpl(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    @Override
    public List<Restaurant> execute(String name) {
        return restaurantGateway.findByName(name);
    }
} 