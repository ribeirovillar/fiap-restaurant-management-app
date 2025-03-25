package fiap.restaurant.app.core.usecase.restaurant;

import fiap.restaurant.app.core.domain.Restaurant;
import fiap.restaurant.app.core.gateway.RestaurantGateway;

import java.util.List;

public class FindRestaurantsByCuisineTypeUseCaseImpl implements FindRestaurantsByCuisineTypeUseCase {

    private final RestaurantGateway restaurantGateway;

    public FindRestaurantsByCuisineTypeUseCaseImpl(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    @Override
    public List<Restaurant> execute(String cuisineType) {
        return restaurantGateway.findByCuisineType(cuisineType);
    }
} 