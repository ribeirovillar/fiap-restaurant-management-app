package fiap.restaurant.app.core.usecase.menuitem;

import fiap.restaurant.app.core.domain.MenuItem;
import fiap.restaurant.app.core.gateway.MenuItemGateway;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FindMenuItemUseCaseImpl implements FindMenuItemUseCase {
    private final MenuItemGateway menuItemGateway;

    public FindMenuItemUseCaseImpl(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    @Override
    public Optional<MenuItem> findByIdAndRestaurantId(UUID id, UUID restaurantId) {
        return menuItemGateway.findByIdAndRestaurantId(id, restaurantId);
    }

    @Override
    public List<MenuItem> findByRestaurantId(UUID restaurantId) {
        return menuItemGateway.findByRestaurantId(restaurantId);
    }
} 