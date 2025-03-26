package fiap.restaurant.app.core.usecase.menuitem;

import fiap.restaurant.app.core.domain.MenuItem;
import java.util.UUID;

public interface UpdateMenuItemUseCase {
    MenuItem execute(UUID restaurantId, UUID id, MenuItem menuItem);
} 