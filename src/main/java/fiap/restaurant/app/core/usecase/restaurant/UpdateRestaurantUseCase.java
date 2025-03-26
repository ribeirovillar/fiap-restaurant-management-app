package fiap.restaurant.app.core.usecase.restaurant;

import fiap.restaurant.app.core.domain.Restaurant;

import java.util.UUID;

public interface UpdateRestaurantUseCase {
    Restaurant execute(UUID id, Restaurant restaurant);
} 