package fiap.restaurant.app.adapter.presenter.impl;

import fiap.restaurant.app.adapter.presenter.MenuItemPresenter;
import fiap.restaurant.app.adapter.web.json.menuitem.MenuItemDTO;
import fiap.restaurant.app.adapter.web.json.menuitem.UpsertMenuItemDTO;
import fiap.restaurant.app.core.domain.MenuItem;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MenuItemPresenterImpl implements MenuItemPresenter {
    @Override
    public MenuItemDTO toDTO(MenuItem menuItem) {
        return MenuItemDTO.builder()
                .id(menuItem.getId())
                .name(menuItem.getName())
                .description(menuItem.getDescription())
                .price(menuItem.getPrice())
                .availableForTakeout(menuItem.isAvailableForTakeout())
                .photoPath(menuItem.getPhotoPath())
                .restaurantId(menuItem.getRestaurant().getId())
                .restaurantName(menuItem.getRestaurant().getName())
                .createdAt(menuItem.getCreatedAt())
                .updatedAt(menuItem.getUpdatedAt())
                .build();
    }

    @Override
    public MenuItem toDomain(UpsertMenuItemDTO dto) {
        MenuItem menuItem = new MenuItem();
        menuItem.setName(dto.getName());
        menuItem.setDescription(dto.getDescription());
        menuItem.setPrice(dto.getPrice());
        menuItem.setAvailableForTakeout(dto.isAvailableForTakeout());
        menuItem.setPhotoPath(dto.getPhotoPath());
        menuItem.setCreatedAt(LocalDateTime.now());
        menuItem.setUpdatedAt(LocalDateTime.now());
        return menuItem;
    }

} 