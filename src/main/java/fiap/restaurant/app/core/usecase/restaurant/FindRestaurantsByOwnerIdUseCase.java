package fiap.restaurant.app.core.usecase.restaurant;

import fiap.restaurant.app.core.domain.Restaurant;

import java.util.List;
import java.util.UUID;

public interface FindRestaurantsByOwnerIdUseCase {
    List<Restaurant> execute(UUID ownerId);
} 