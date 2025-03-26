package fiap.restaurant.app.core.usecase.menuitem;

import fiap.restaurant.app.core.domain.MenuItem;
import fiap.restaurant.app.core.domain.Restaurant;
import fiap.restaurant.app.core.gateway.MenuItemGateway;
import fiap.restaurant.app.core.gateway.RestaurantGateway;

import java.util.UUID;

public class CreateMenuItemUseCaseImpl implements CreateMenuItemUseCase {
    private final MenuItemGateway menuItemGateway;
    private final RestaurantGateway restaurantGateway;

    public CreateMenuItemUseCaseImpl(MenuItemGateway menuItemGateway, RestaurantGateway restaurantGateway) {
        this.menuItemGateway = menuItemGateway;
        this.restaurantGateway = restaurantGateway;
    }

    @Override
    public MenuItem execute(MenuItem menuItem, UUID restaurantId) {
        Restaurant restaurant = restaurantGateway.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        menuItem.setRestaurant(restaurant);

        menuItem.validateForCreation();

        return menuItemGateway.save(menuItem);
    }
} 