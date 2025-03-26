package fiap.restaurant.app.core.usecase.menuitem;

import java.util.UUID;

public interface DeleteMenuItemUseCase {
    void execute(UUID id, UUID restaurantId);
} 