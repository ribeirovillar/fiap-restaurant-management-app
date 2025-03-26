package fiap.restaurant.app.core.gateway;

import fiap.restaurant.app.core.domain.MenuItem;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuItemGateway {
    MenuItem save(MenuItem menuItem);
    Optional<MenuItem> findByIdAndRestaurantId(UUID id, UUID restaurantId);
    List<MenuItem> findByRestaurantId(UUID restaurantId);
    void delete(UUID id);
} 