package fiap.restaurant.app.core.controller;

import fiap.restaurant.app.core.domain.MenuItem;
import fiap.restaurant.app.core.usecase.menuitem.CreateMenuItemUseCase;
import fiap.restaurant.app.core.usecase.menuitem.DeleteMenuItemUseCase;
import fiap.restaurant.app.core.usecase.menuitem.FindMenuItemUseCase;
import fiap.restaurant.app.core.usecase.menuitem.UpdateMenuItemUseCase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MenuItemController {
    private final CreateMenuItemUseCase createMenuItemUseCase;
    private final FindMenuItemUseCase findMenuItemUseCase;
    private final UpdateMenuItemUseCase updateMenuItemUseCase;
    private final DeleteMenuItemUseCase deleteMenuItemUseCase;

    public MenuItemController(
            CreateMenuItemUseCase createMenuItemUseCase,
            FindMenuItemUseCase findMenuItemUseCase,
            UpdateMenuItemUseCase updateMenuItemUseCase,
            DeleteMenuItemUseCase deleteMenuItemUseCase) {
        this.createMenuItemUseCase = createMenuItemUseCase;
        this.findMenuItemUseCase = findMenuItemUseCase;
        this.updateMenuItemUseCase = updateMenuItemUseCase;
        this.deleteMenuItemUseCase = deleteMenuItemUseCase;
    }

    public MenuItem create(MenuItem menuItem, UUID restaurantId) {
        return createMenuItemUseCase.execute(menuItem, restaurantId);
    }

    public Optional<MenuItem> findByIdAndRestaurantId(UUID id, UUID restaurantId) {
        return findMenuItemUseCase.findByIdAndRestaurantId(id, restaurantId);
    }

    public List<MenuItem> findByRestaurantId(UUID restaurantId) {
        return findMenuItemUseCase.findByRestaurantId(restaurantId);
    }

    public MenuItem update(UUID restaurantId, UUID id, MenuItem menuItem) {
        return updateMenuItemUseCase.execute(restaurantId, id, menuItem);
    }

    public void delete(UUID id, UUID restaurantId) {
        deleteMenuItemUseCase.execute(id, restaurantId);
    }
} 