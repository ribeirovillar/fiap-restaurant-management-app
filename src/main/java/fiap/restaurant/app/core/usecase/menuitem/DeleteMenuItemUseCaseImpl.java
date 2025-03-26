package fiap.restaurant.app.core.usecase.menuitem;

import fiap.restaurant.app.core.domain.MenuItem;
import fiap.restaurant.app.core.gateway.MenuItemGateway;

import java.util.UUID;

public class DeleteMenuItemUseCaseImpl implements DeleteMenuItemUseCase {
    private final MenuItemGateway menuItemGateway;

    public DeleteMenuItemUseCaseImpl(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    @Override
    public void execute(UUID id, UUID restaurantId) {
        MenuItem menuItem = menuItemGateway.findByIdAndRestaurantId(id, restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found"));

        menuItem.validateForDelete();
        menuItemGateway.delete(id);
    }
} 