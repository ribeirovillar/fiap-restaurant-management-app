package fiap.restaurant.app.core.usecase.menuitem;

import fiap.restaurant.app.core.domain.MenuItem;

import java.util.UUID;

public interface CreateMenuItemUseCase {
    MenuItem execute(MenuItem menuItem, UUID restaurantId);
} 