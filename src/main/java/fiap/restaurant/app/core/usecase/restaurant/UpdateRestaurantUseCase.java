package fiap.restaurant.app.core.usecase.restaurant;

import fiap.restaurant.app.core.domain.Restaurant;

public interface UpdateRestaurantUseCase {
    Restaurant execute(Restaurant restaurant);
} 