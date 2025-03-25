package fiap.restaurant.app.core.usecase.restaurant;

import fiap.restaurant.app.core.domain.Restaurant;

public interface CreateRestaurantUseCase {
    Restaurant execute(Restaurant restaurant);
} 