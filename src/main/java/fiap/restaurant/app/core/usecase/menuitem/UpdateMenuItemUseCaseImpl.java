package fiap.restaurant.app.core.usecase.menuitem;

import fiap.restaurant.app.core.domain.MenuItem;
import fiap.restaurant.app.core.gateway.MenuItemGateway;

import java.util.UUID;

public class UpdateMenuItemUseCaseImpl implements UpdateMenuItemUseCase {
    private final MenuItemGateway menuItemGateway;

    public UpdateMenuItemUseCaseImpl(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    @Override
    public MenuItem execute(UUID restaurantId, UUID id, MenuItem menuItem) {
        MenuItem existingMenuItem = menuItemGateway.findByIdAndRestaurantId(id, restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found"));

        menuItem.setId(id);
        menuItem.setCreatedAt(existingMenuItem.getCreatedAt());
        menuItem.setRestaurant(existingMenuItem.getRestaurant());
        menuItem.validateForUpdate();
        return menuItemGateway.save(menuItem);
    }
} 