package fiap.restaurant.app.core.usecase.restaurant;

import fiap.restaurant.app.core.domain.Restaurant;
import java.util.UUID;

public interface FindRestaurantByIdUseCase {
    Restaurant execute(UUID id);
} 