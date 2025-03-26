package fiap.restaurant.app.adapter.web;

import fiap.restaurant.app.adapter.presenter.MenuItemPresenter;
import fiap.restaurant.app.adapter.web.json.menuitem.MenuItemDTO;
import fiap.restaurant.app.adapter.web.json.menuitem.UpsertMenuItemDTO;
import fiap.restaurant.app.core.controller.MenuItemController;
import fiap.restaurant.app.core.domain.MenuItem;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/restaurants/{restaurantId}/menu-items")
@Tag(name = "Menu Items", description = "API for menu items management")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class MenuItemApiController {
    MenuItemController menuItemController;
    MenuItemPresenter menuItemPresenter;

    @PostMapping
    public ResponseEntity<MenuItemDTO> createMenuItem(
            @PathVariable UUID restaurantId,
            @RequestBody UpsertMenuItemDTO createMenuItemDTO) {
        MenuItem menuItem = menuItemPresenter.toDomain(createMenuItemDTO);
        MenuItem createdMenuItem = menuItemController.create(menuItem, restaurantId);
        return ResponseEntity.ok(menuItemPresenter.toDTO(createdMenuItem));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDTO> findMenuItemById(
            @PathVariable UUID restaurantId,
            @PathVariable UUID id) {
        return menuItemController.findByIdAndRestaurantId(id, restaurantId)
                .map(menuItemPresenter::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<MenuItemDTO>> findMenuItemsByRestaurantId(@PathVariable UUID restaurantId) {
        List<MenuItemDTO> menuItems = menuItemController.findByRestaurantId(restaurantId)
                .stream()
                .map(menuItemPresenter::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(menuItems);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemDTO> updateMenuItem(
            @PathVariable UUID restaurantId,
            @PathVariable UUID id,
            @RequestBody UpsertMenuItemDTO updateMenuItemDTO) {
        MenuItem menuItem = menuItemPresenter.toDomain(updateMenuItemDTO);
        MenuItem updatedMenuItem = menuItemController.update(restaurantId, id, menuItem);
        return ResponseEntity.ok(menuItemPresenter.toDTO(updatedMenuItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(
            @PathVariable UUID restaurantId,
            @PathVariable UUID id) {
        menuItemController.delete(id, restaurantId);
        return ResponseEntity.noContent().build();
    }
} 