package fiap.restaurant.app.core.usecase.menuitem;

import fiap.restaurant.app.core.domain.MenuItem;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FindMenuItemUseCase {
    Optional<MenuItem> findByIdAndRestaurantId(UUID id, UUID restaurantId);
    List<MenuItem> findByRestaurantId(UUID restaurantId);
} 