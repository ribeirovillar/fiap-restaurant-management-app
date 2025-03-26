package fiap.restaurant.app.adapter.database.jpa;

import fiap.restaurant.app.adapter.database.jpa.entity.MenuItemEntity;
import fiap.restaurant.app.adapter.database.jpa.entity.RestaurantEntity;
import fiap.restaurant.app.adapter.database.jpa.repository.MenuItemRepository;
import fiap.restaurant.app.core.domain.MenuItem;
import fiap.restaurant.app.core.domain.Restaurant;
import fiap.restaurant.app.core.gateway.MenuItemGateway;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MenuItemJpaGateway implements MenuItemGateway {
    private final MenuItemRepository menuItemRepository;

    public MenuItemJpaGateway(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        MenuItemEntity entity = toEntity(menuItem);
        MenuItemEntity savedEntity = menuItemRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<MenuItem> findByIdAndRestaurantId(UUID id, UUID restaurantId) {
        return Optional.ofNullable(menuItemRepository.findByIdAndRestaurantId(id, restaurantId)).map(this::toDomain);
    }

    @Override
    public List<MenuItem> findByRestaurantId(UUID restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        menuItemRepository.deleteById(id);
    }

    private MenuItemEntity toEntity(MenuItem menuItem) {
        return MenuItemEntity.builder()
                .id(menuItem.getId())
                .name(menuItem.getName())
                .description(menuItem.getDescription())
                .price(menuItem.getPrice())
                .availableForTakeout(menuItem.isAvailableForTakeout())
                .photoPath(menuItem.getPhotoPath())
                .restaurant(RestaurantEntity.builder()
                        .id(menuItem.getRestaurant().getId())
                        .name(menuItem.getRestaurant().getName())
                        .cuisineType(menuItem.getRestaurant().getCuisineType())
                        .build())
                .createdAt(menuItem.getCreatedAt())
                .updatedAt(menuItem.getUpdatedAt())
                .build();
    }

    private MenuItem toDomain(MenuItemEntity entity) {
        MenuItem menuItem = new MenuItem();
        menuItem.setId(entity.getId());
        menuItem.setName(entity.getName());
        menuItem.setDescription(entity.getDescription());
        menuItem.setPrice(entity.getPrice());
        menuItem.setAvailableForTakeout(entity.isAvailableForTakeout());
        menuItem.setPhotoPath(entity.getPhotoPath());
        menuItem.setRestaurant(Restaurant.builder()
                .id(entity.getRestaurant().getId())
                .name(entity.getRestaurant().getName())
                .cuisineType(entity.getRestaurant().getCuisineType())
                .build());
        menuItem.setCreatedAt(entity.getCreatedAt());
        menuItem.setUpdatedAt(entity.getUpdatedAt());
        return menuItem;
    }
} 