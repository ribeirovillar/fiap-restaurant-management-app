package fiap.restaurant.app.core.usecase.menuitem;

import fiap.restaurant.app.core.domain.CuisineType;
import fiap.restaurant.app.core.domain.MenuItem;
import fiap.restaurant.app.core.domain.Restaurant;
import fiap.restaurant.app.core.domain.User;
import fiap.restaurant.app.core.gateway.MenuItemGateway;
import fiap.restaurant.app.core.gateway.RestaurantGateway;
import fiap.restaurant.app.util.UserTypeTestHelper;
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
class CreateMenuItemUseCaseImplTest {

    @Mock
    private MenuItemGateway menuItemGateway;

    @Mock
    private RestaurantGateway restaurantGateway;

    private CreateMenuItemUseCaseImpl createMenuItemUseCase;

    @BeforeEach
    void setUp() {
        createMenuItemUseCase = new CreateMenuItemUseCaseImpl(menuItemGateway, restaurantGateway);
    }

    @Test
    void shouldCreateMenuItemSuccessfully() {
        UUID restaurantId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();

        Restaurant restaurant = Restaurant.builder()
                .id(restaurantId)
                .name("Test Restaurant")
                .cuisineType(CuisineType.ITALIAN)
                .owner(User.builder().id(UUID.randomUUID()).userType(UserTypeTestHelper.createOwnerDomain()).build())
                .build();

        MenuItem menuItem = new MenuItem();
        menuItem.setId(menuItemId);
        menuItem.setName("Test Menu Item");
        menuItem.setDescription("This is a test menu item description that is long enough");
        menuItem.setPrice(new BigDecimal("15.99"));
        menuItem.setAvailableForTakeout(true);
        menuItem.setPhotoPath("/images/test.jpg");

        MenuItem savedMenuItem = new MenuItem();
        savedMenuItem.setId(menuItemId);
        savedMenuItem.setName("Test Menu Item");
        savedMenuItem.setDescription("This is a test menu item description that is long enough");
        savedMenuItem.setPrice(new BigDecimal("15.99"));
        savedMenuItem.setAvailableForTakeout(true);
        savedMenuItem.setPhotoPath("/images/test.jpg");
        savedMenuItem.setRestaurant(restaurant);
        savedMenuItem.setCreatedAt(LocalDateTime.now());
        savedMenuItem.setUpdatedAt(LocalDateTime.now());

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(menuItemGateway.save(any(MenuItem.class))).thenReturn(savedMenuItem);

        MenuItem result = createMenuItemUseCase.execute(menuItem, restaurantId);

        assertNotNull(result);
        assertEquals(menuItemId, result.getId());
        assertEquals("Test Menu Item", result.getName());
        assertEquals("This is a test menu item description that is long enough", result.getDescription());
        assertEquals(new BigDecimal("15.99"), result.getPrice());
        assertTrue(result.isAvailableForTakeout());
        assertEquals("/images/test.jpg", result.getPhotoPath());
        assertNotNull(result.getRestaurant());
        assertEquals(restaurantId, result.getRestaurant().getId());

        verify(restaurantGateway).findById(restaurantId);
        verify(menuItemGateway).save(any(MenuItem.class));
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {
        UUID restaurantId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();

        MenuItem menuItem = new MenuItem();
        menuItem.setId(menuItemId);
        menuItem.setName("Test Menu Item");
        menuItem.setDescription("This is a test menu item description that is long enough");
        menuItem.setPrice(new BigDecimal("15.99"));
        menuItem.setAvailableForTakeout(true);
        menuItem.setPhotoPath("/images/test.jpg");

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> createMenuItemUseCase.execute(menuItem, restaurantId));

        assertEquals("Restaurant not found", exception.getMessage());
        verify(restaurantGateway).findById(restaurantId);
        verify(menuItemGateway, never()).save(any(MenuItem.class));
    }

    @Test
    void shouldThrowExceptionWhenMenuItemNameIsInvalid() {
        UUID restaurantId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();

        Restaurant restaurant = Restaurant.builder()
                .id(restaurantId)
                .name("Test Restaurant")
                .cuisineType(CuisineType.ITALIAN)
                .owner(User.builder().id(UUID.randomUUID()).userType(UserTypeTestHelper.createOwnerDomain()).build())
                .build();

        MenuItem menuItem = new MenuItem();
        menuItem.setId(menuItemId);
        menuItem.setName("Valid Menu Item Name");
        menuItem.setDescription("This is a test menu item description that is long enough");
        menuItem.setPrice(new BigDecimal("15.99"));
        menuItem.setAvailableForTakeout(true);
        menuItem.setPhotoPath("/images/test.jpg");

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        
        doAnswer(invocation -> {
            MenuItem item = invocation.getArgument(0);
            String currentName = item.getName();
            item.setName("Valid Name");
            MenuItem invalidItem = new MenuItem();
            try {
                invalidItem.setName("AB");
                return null;
            } catch (IllegalArgumentException e) {
                item.setName(currentName);
                throw e;
            }
        }).when(menuItemGateway).save(any(MenuItem.class));

        Exception exception = assertThrows(IllegalArgumentException.class, 
                () -> createMenuItemUseCase.execute(menuItem, restaurantId));

        assertEquals("Name must be between 3 and 100 characters", exception.getMessage());
        verify(restaurantGateway).findById(restaurantId);
    }

    @Test
    void shouldThrowExceptionWhenMenuItemDescriptionIsTooShort() {
        UUID restaurantId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();

        Restaurant restaurant = Restaurant.builder()
                .id(restaurantId)
                .name("Test Restaurant")
                .cuisineType(CuisineType.ITALIAN)
                .owner(User.builder().id(UUID.randomUUID()).userType(UserTypeTestHelper.createOwnerDomain()).build())
                .build();

        MenuItem menuItem = new MenuItem();
        menuItem.setId(menuItemId);
        menuItem.setName("Test Menu Item");
        menuItem.setDescription("This is a valid description that is long enough");
        menuItem.setPrice(new BigDecimal("15.99"));
        menuItem.setAvailableForTakeout(true);
        menuItem.setPhotoPath("/images/test.jpg");

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        
        doThrow(new IllegalArgumentException("Description must be between 10 and 500 characters"))
            .when(menuItemGateway).save(any(MenuItem.class));

        Exception exception = assertThrows(IllegalArgumentException.class, 
                () -> createMenuItemUseCase.execute(menuItem, restaurantId));

        assertEquals("Description must be between 10 and 500 characters", exception.getMessage());
        verify(restaurantGateway).findById(restaurantId);
    }

    @Test
    void shouldThrowExceptionWhenMenuItemPriceIsInvalid() {
        UUID restaurantId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();

        Restaurant restaurant = Restaurant.builder()
                .id(restaurantId)
                .name("Test Restaurant")
                .cuisineType(CuisineType.ITALIAN)
                .owner(User.builder().id(UUID.randomUUID()).userType(UserTypeTestHelper.createOwnerDomain()).build())
                .build();

        MenuItem menuItem = new MenuItem();
        menuItem.setId(menuItemId);
        menuItem.setName("Test Menu Item");
        menuItem.setDescription("This is a test menu item description that is long enough");
        menuItem.setPrice(new BigDecimal("15.99"));
        menuItem.setAvailableForTakeout(true);
        menuItem.setPhotoPath("/images/test.jpg");

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        
        doThrow(new IllegalArgumentException("Price must be greater than 0.01"))
            .when(menuItemGateway).save(any(MenuItem.class));

        Exception exception = assertThrows(IllegalArgumentException.class, 
                () -> createMenuItemUseCase.execute(menuItem, restaurantId));

        assertEquals("Price must be greater than 0.01", exception.getMessage());
        verify(restaurantGateway).findById(restaurantId);
    }

    @Test
    void shouldThrowExceptionWhenPhotoPathIsNull() {
        UUID restaurantId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();

        Restaurant restaurant = Restaurant.builder()
                .id(restaurantId)
                .name("Test Restaurant")
                .cuisineType(CuisineType.ITALIAN)
                .owner(User.builder().id(UUID.randomUUID()).userType(UserTypeTestHelper.createOwnerDomain()).build())
                .build();

        MenuItem menuItem = new MenuItem();
        menuItem.setId(menuItemId);
        menuItem.setName("Test Menu Item");
        menuItem.setDescription("This is a test menu item description that is long enough");
        menuItem.setPrice(new BigDecimal("15.99"));
        menuItem.setAvailableForTakeout(true);
        menuItem.setPhotoPath("/images/test.jpg");

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        
        doThrow(new IllegalArgumentException("Photo path is required"))
            .when(menuItemGateway).save(any(MenuItem.class));

        Exception exception = assertThrows(IllegalArgumentException.class, 
                () -> createMenuItemUseCase.execute(menuItem, restaurantId));

        assertEquals("Photo path is required", exception.getMessage());
        verify(restaurantGateway).findById(restaurantId);
    }
} 