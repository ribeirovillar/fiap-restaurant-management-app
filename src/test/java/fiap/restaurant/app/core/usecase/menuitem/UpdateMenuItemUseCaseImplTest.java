package fiap.restaurant.app.core.usecase.menuitem;

import fiap.restaurant.app.core.domain.CuisineType;
import fiap.restaurant.app.core.domain.MenuItem;
import fiap.restaurant.app.core.domain.Restaurant;
import fiap.restaurant.app.core.domain.User;
import fiap.restaurant.app.core.domain.UserType;
import fiap.restaurant.app.core.gateway.MenuItemGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateMenuItemUseCaseImplTest {

    @Mock
    private MenuItemGateway menuItemGateway;

    private UpdateMenuItemUseCaseImpl updateMenuItemUseCase;

    @BeforeEach
    void setUp() {
        updateMenuItemUseCase = new UpdateMenuItemUseCaseImpl(menuItemGateway);
    }

    @Test
    void shouldUpdateMenuItemSuccessfully() {
        UUID restaurantId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Restaurant restaurant = Restaurant.builder()
                .id(restaurantId)
                .name("Test Restaurant")
                .cuisineType(CuisineType.ITALIAN)
                .owner(User.builder().id(ownerId).userType(UserType.OWNER).build())
                .build();

        MenuItem existingMenuItem = new MenuItem();
        existingMenuItem.setId(menuItemId);
        existingMenuItem.setName("Original Menu Item");
        existingMenuItem.setDescription("This is the original description for the menu item");
        existingMenuItem.setPrice(new BigDecimal("12.99"));
        existingMenuItem.setAvailableForTakeout(true);
        existingMenuItem.setPhotoPath("/images/original.jpg");
        existingMenuItem.setRestaurant(restaurant);
        existingMenuItem.setCreatedAt(LocalDateTime.now().minusDays(1));
        existingMenuItem.setUpdatedAt(LocalDateTime.now().minusDays(1));

        MenuItem updatedMenuItemRequest = new MenuItem();
        updatedMenuItemRequest.setName("Updated Menu Item");
        updatedMenuItemRequest.setDescription("This is the updated description for the menu item");
        updatedMenuItemRequest.setPrice(new BigDecimal("15.99"));
        updatedMenuItemRequest.setAvailableForTakeout(false);
        updatedMenuItemRequest.setPhotoPath("/images/updated.jpg");

        MenuItem savedMenuItem = new MenuItem();
        savedMenuItem.setId(menuItemId);
        savedMenuItem.setName("Updated Menu Item");
        savedMenuItem.setDescription("This is the updated description for the menu item");
        savedMenuItem.setPrice(new BigDecimal("15.99"));
        savedMenuItem.setAvailableForTakeout(false);
        savedMenuItem.setPhotoPath("/images/updated.jpg");
        savedMenuItem.setRestaurant(restaurant);
        savedMenuItem.setCreatedAt(existingMenuItem.getCreatedAt());
        savedMenuItem.setUpdatedAt(LocalDateTime.now());

        when(menuItemGateway.findByIdAndRestaurantId(menuItemId, restaurantId)).thenReturn(Optional.of(existingMenuItem));
        when(menuItemGateway.save(any(MenuItem.class))).thenReturn(savedMenuItem);

        MenuItem result = updateMenuItemUseCase.execute(restaurantId, menuItemId, updatedMenuItemRequest);

        assertNotNull(result);
        assertEquals(menuItemId, result.getId());
        assertEquals("Updated Menu Item", result.getName());
        assertEquals("This is the updated description for the menu item", result.getDescription());
        assertEquals(new BigDecimal("15.99"), result.getPrice());
        assertFalse(result.isAvailableForTakeout());
        assertEquals("/images/updated.jpg", result.getPhotoPath());
        assertNotNull(result.getRestaurant());
        assertEquals(restaurantId, result.getRestaurant().getId());
        assertEquals(existingMenuItem.getCreatedAt(), result.getCreatedAt());

        verify(menuItemGateway).findByIdAndRestaurantId(menuItemId, restaurantId);
        verify(menuItemGateway).save(any(MenuItem.class));
    }

    @Test
    void shouldThrowExceptionWhenMenuItemNotFound() {
        UUID restaurantId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();

        MenuItem updatedMenuItemRequest = new MenuItem();
        updatedMenuItemRequest.setName("Updated Menu Item");
        updatedMenuItemRequest.setDescription("This is the updated description for the menu item");
        updatedMenuItemRequest.setPrice(new BigDecimal("15.99"));
        updatedMenuItemRequest.setAvailableForTakeout(false);
        updatedMenuItemRequest.setPhotoPath("/images/updated.jpg");

        when(menuItemGateway.findByIdAndRestaurantId(menuItemId, restaurantId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> updateMenuItemUseCase.execute(restaurantId, menuItemId, updatedMenuItemRequest));

        assertEquals("Menu item not found", exception.getMessage());
        verify(menuItemGateway).findByIdAndRestaurantId(menuItemId, restaurantId);
        verify(menuItemGateway, never()).save(any(MenuItem.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatedMenuItemNameIsInvalid() {
        UUID restaurantId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Restaurant restaurant = Restaurant.builder()
                .id(restaurantId)
                .name("Test Restaurant")
                .cuisineType(CuisineType.ITALIAN)
                .owner(User.builder().id(ownerId).userType(UserType.OWNER).build())
                .build();

        MenuItem existingMenuItem = new MenuItem();
        existingMenuItem.setId(menuItemId);
        existingMenuItem.setName("Original Menu Item");
        existingMenuItem.setDescription("This is the original description for the menu item");
        existingMenuItem.setPrice(new BigDecimal("12.99"));
        existingMenuItem.setAvailableForTakeout(true);
        existingMenuItem.setPhotoPath("/images/original.jpg");
        existingMenuItem.setRestaurant(restaurant);
        existingMenuItem.setCreatedAt(LocalDateTime.now().minusDays(1));
        existingMenuItem.setUpdatedAt(LocalDateTime.now().minusDays(1));

        MenuItem updatedMenuItemRequest = new MenuItem();
        updatedMenuItemRequest.setName("Valid Name");
        updatedMenuItemRequest.setDescription("This is the updated description for the menu item");
        updatedMenuItemRequest.setPrice(new BigDecimal("15.99"));
        updatedMenuItemRequest.setAvailableForTakeout(false);
        updatedMenuItemRequest.setPhotoPath("/images/updated.jpg");

        when(menuItemGateway.findByIdAndRestaurantId(menuItemId, restaurantId)).thenReturn(Optional.of(existingMenuItem));
        
        doThrow(new IllegalArgumentException("Name must be between 3 and 100 characters"))
            .when(menuItemGateway).save(any(MenuItem.class));

        Exception exception = assertThrows(IllegalArgumentException.class, 
                () -> updateMenuItemUseCase.execute(restaurantId, menuItemId, updatedMenuItemRequest));

        assertEquals("Name must be between 3 and 100 characters", exception.getMessage());
        verify(menuItemGateway).findByIdAndRestaurantId(menuItemId, restaurantId);
    }

    @Test
    void shouldThrowExceptionWhenUpdatedMenuItemDescriptionIsTooShort() {
        UUID restaurantId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Restaurant restaurant = Restaurant.builder()
                .id(restaurantId)
                .name("Test Restaurant")
                .cuisineType(CuisineType.ITALIAN)
                .owner(User.builder().id(ownerId).userType(UserType.OWNER).build())
                .build();

        MenuItem existingMenuItem = new MenuItem();
        existingMenuItem.setId(menuItemId);
        existingMenuItem.setName("Original Menu Item");
        existingMenuItem.setDescription("This is the original description for the menu item");
        existingMenuItem.setPrice(new BigDecimal("12.99"));
        existingMenuItem.setAvailableForTakeout(true);
        existingMenuItem.setPhotoPath("/images/original.jpg");
        existingMenuItem.setRestaurant(restaurant);
        existingMenuItem.setCreatedAt(LocalDateTime.now().minusDays(1));
        existingMenuItem.setUpdatedAt(LocalDateTime.now().minusDays(1));

        MenuItem updatedMenuItemRequest = new MenuItem();
        updatedMenuItemRequest.setName("Updated Menu Item");
        updatedMenuItemRequest.setDescription("Valid description that is long enough");
        updatedMenuItemRequest.setPrice(new BigDecimal("15.99"));
        updatedMenuItemRequest.setAvailableForTakeout(false);
        updatedMenuItemRequest.setPhotoPath("/images/updated.jpg");

        when(menuItemGateway.findByIdAndRestaurantId(menuItemId, restaurantId)).thenReturn(Optional.of(existingMenuItem));
        
        doThrow(new IllegalArgumentException("Description must be between 10 and 500 characters"))
            .when(menuItemGateway).save(any(MenuItem.class));

        Exception exception = assertThrows(IllegalArgumentException.class, 
                () -> updateMenuItemUseCase.execute(restaurantId, menuItemId, updatedMenuItemRequest));

        assertEquals("Description must be between 10 and 500 characters", exception.getMessage());
        verify(menuItemGateway).findByIdAndRestaurantId(menuItemId, restaurantId);
    }

    @Test
    void shouldThrowExceptionWhenUpdatedMenuItemPriceIsInvalid() {
        UUID restaurantId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Restaurant restaurant = Restaurant.builder()
                .id(restaurantId)
                .name("Test Restaurant")
                .cuisineType(CuisineType.ITALIAN)
                .owner(User.builder().id(ownerId).userType(UserType.OWNER).build())
                .build();

        MenuItem existingMenuItem = new MenuItem();
        existingMenuItem.setId(menuItemId);
        existingMenuItem.setName("Original Menu Item");
        existingMenuItem.setDescription("This is the original description for the menu item");
        existingMenuItem.setPrice(new BigDecimal("12.99"));
        existingMenuItem.setAvailableForTakeout(true);
        existingMenuItem.setPhotoPath("/images/original.jpg");
        existingMenuItem.setRestaurant(restaurant);
        existingMenuItem.setCreatedAt(LocalDateTime.now().minusDays(1));
        existingMenuItem.setUpdatedAt(LocalDateTime.now().minusDays(1));

        MenuItem updatedMenuItemRequest = new MenuItem();
        updatedMenuItemRequest.setName("Updated Menu Item");
        updatedMenuItemRequest.setDescription("This is the updated description for the menu item");
        updatedMenuItemRequest.setPrice(new BigDecimal("15.99"));
        updatedMenuItemRequest.setAvailableForTakeout(false);
        updatedMenuItemRequest.setPhotoPath("/images/updated.jpg");

        when(menuItemGateway.findByIdAndRestaurantId(menuItemId, restaurantId)).thenReturn(Optional.of(existingMenuItem));
        
        doThrow(new IllegalArgumentException("Price must be greater than 0.01"))
            .when(menuItemGateway).save(any(MenuItem.class));

        Exception exception = assertThrows(IllegalArgumentException.class, 
                () -> updateMenuItemUseCase.execute(restaurantId, menuItemId, updatedMenuItemRequest));

        assertEquals("Price must be greater than 0.01", exception.getMessage());
        verify(menuItemGateway).findByIdAndRestaurantId(menuItemId, restaurantId);
    }

    @Test
    void shouldThrowExceptionWhenUpdatedMenuItemPhotoPathIsNull() {
        UUID restaurantId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();

        Restaurant restaurant = Restaurant.builder()
                .id(restaurantId)
                .name("Test Restaurant")
                .cuisineType(CuisineType.ITALIAN)
                .owner(User.builder().id(ownerId).userType(UserType.OWNER).build())
                .build();

        MenuItem existingMenuItem = new MenuItem();
        existingMenuItem.setId(menuItemId);
        existingMenuItem.setName("Original Menu Item");
        existingMenuItem.setDescription("This is the original description for the menu item");
        existingMenuItem.setPrice(new BigDecimal("12.99"));
        existingMenuItem.setAvailableForTakeout(true);
        existingMenuItem.setPhotoPath("/images/original.jpg");
        existingMenuItem.setRestaurant(restaurant);
        existingMenuItem.setCreatedAt(LocalDateTime.now().minusDays(1));
        existingMenuItem.setUpdatedAt(LocalDateTime.now().minusDays(1));

        MenuItem updatedMenuItemRequest = new MenuItem();
        updatedMenuItemRequest.setName("Updated Menu Item");
        updatedMenuItemRequest.setDescription("This is the updated description for the menu item");
        updatedMenuItemRequest.setPrice(new BigDecimal("15.99"));
        updatedMenuItemRequest.setAvailableForTakeout(false);
        updatedMenuItemRequest.setPhotoPath("/images/updated.jpg");

        when(menuItemGateway.findByIdAndRestaurantId(menuItemId, restaurantId)).thenReturn(Optional.of(existingMenuItem));
        
        doThrow(new IllegalArgumentException("Photo path is required"))
            .when(menuItemGateway).save(any(MenuItem.class));

        Exception exception = assertThrows(IllegalArgumentException.class, 
                () -> updateMenuItemUseCase.execute(restaurantId, menuItemId, updatedMenuItemRequest));

        assertEquals("Photo path is required", exception.getMessage());
        verify(menuItemGateway).findByIdAndRestaurantId(menuItemId, restaurantId);
    }
} 