package fiap.restaurant.app.core.usecase.restaurant;

import fiap.restaurant.app.core.domain.Restaurant;

import java.util.List;

public interface FindRestaurantsByNameUseCase {
    List<Restaurant> execute(String name);
} 