package fiap.restaurant.app.core.usecase.restaurant;

import fiap.restaurant.app.core.domain.Restaurant;

import java.util.List;

public interface FindRestaurantsByCuisineTypeUseCase {
    List<Restaurant> execute(String cuisineType);
} 