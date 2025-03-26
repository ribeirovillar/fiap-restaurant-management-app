package fiap.restaurant.app.configuration;

import fiap.restaurant.app.core.controller.MenuItemController;
import fiap.restaurant.app.core.gateway.MenuItemGateway;
import fiap.restaurant.app.core.gateway.RestaurantGateway;
import fiap.restaurant.app.core.usecase.menuitem.CreateMenuItemUseCase;
import fiap.restaurant.app.core.usecase.menuitem.CreateMenuItemUseCaseImpl;
import fiap.restaurant.app.core.usecase.menuitem.DeleteMenuItemUseCase;
import fiap.restaurant.app.core.usecase.menuitem.DeleteMenuItemUseCaseImpl;
import fiap.restaurant.app.core.usecase.menuitem.FindMenuItemUseCase;
import fiap.restaurant.app.core.usecase.menuitem.FindMenuItemUseCaseImpl;
import fiap.restaurant.app.core.usecase.menuitem.UpdateMenuItemUseCase;
import fiap.restaurant.app.core.usecase.menuitem.UpdateMenuItemUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MenuItemBeanConfiguration {

    @Bean
    public CreateMenuItemUseCase createMenuItemUseCase(MenuItemGateway menuItemGateway, RestaurantGateway restaurantGateway) {
        return new CreateMenuItemUseCaseImpl(menuItemGateway, restaurantGateway);
    }

    @Bean
    public FindMenuItemUseCase findMenuItemUseCase(MenuItemGateway menuItemGateway) {
        return new FindMenuItemUseCaseImpl(menuItemGateway);
    }

    @Bean
    public UpdateMenuItemUseCase updateMenuItemUseCase(MenuItemGateway menuItemGateway) {
        return new UpdateMenuItemUseCaseImpl(menuItemGateway);
    }

    @Bean
    public DeleteMenuItemUseCase deleteMenuItemUseCase(MenuItemGateway menuItemGateway) {
        return new DeleteMenuItemUseCaseImpl(menuItemGateway);
    }

    @Bean
    public MenuItemController menuItemController(
            CreateMenuItemUseCase createMenuItemUseCase,
            FindMenuItemUseCase findMenuItemUseCase,
            UpdateMenuItemUseCase updateMenuItemUseCase,
            DeleteMenuItemUseCase deleteMenuItemUseCase) {
        return new MenuItemController(
                createMenuItemUseCase,
                findMenuItemUseCase,
                updateMenuItemUseCase,
                deleteMenuItemUseCase
        );
    }
} 