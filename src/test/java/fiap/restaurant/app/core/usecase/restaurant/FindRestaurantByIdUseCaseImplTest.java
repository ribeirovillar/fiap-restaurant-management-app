package fiap.restaurant.app.core.usecase.restaurant;

import fiap.restaurant.app.core.domain.CuisineType;
import fiap.restaurant.app.core.domain.Restaurant;
import fiap.restaurant.app.core.domain.User;
import fiap.restaurant.app.core.exception.RestaurantNotFoundException;
import fiap.restaurant.app.core.gateway.RestaurantGateway;
import fiap.restaurant.app.util.UserTypeTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindRestaurantByIdUseCaseImplTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    private FindRestaurantByIdUseCaseImpl findRestaurantByIdUseCase;

    @BeforeEach
    void setUp() {
        findRestaurantByIdUseCase = new FindRestaurantByIdUseCaseImpl(restaurantGateway);
    }

    @Test
    void shouldFindRestaurantByIdSuccessfully() {
        UUID restaurantId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        
        User owner = User.builder()
                .id(ownerId)
                .name("Restaurant Owner")
                .email("owner@example.com")
                .userType(UserTypeTestHelper.createOwnerDomain())
                .build();
        
        Restaurant expectedRestaurant = Restaurant.builder()
                .id(restaurantId)
                .name("Test Restaurant")
                .cuisineType(CuisineType.ITALIAN)
                .owner(owner)
                .createdAt("2023-01-01T12:00:00")
                .updatedAt("2023-01-01T12:00:00")
                .build();
        
        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(expectedRestaurant));
        
        Restaurant result = findRestaurantByIdUseCase.execute(restaurantId);
        
        assertNotNull(result);
        assertEquals(restaurantId, result.getId());
        assertEquals("Test Restaurant", result.getName());
        assertEquals(CuisineType.ITALIAN, result.getCuisineType());
        assertEquals(ownerId, result.getOwner().getId());
        
        verify(restaurantGateway).findById(restaurantId);
    }
    
    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {
        UUID nonExistingId = UUID.randomUUID();
        
        when(restaurantGateway.findById(nonExistingId)).thenReturn(Optional.empty());
        
        RestaurantNotFoundException exception = assertThrows(RestaurantNotFoundException.class,
                () -> findRestaurantByIdUseCase.execute(nonExistingId));
        
        assertEquals("Restaurant not found with id: " + nonExistingId, exception.getMessage());
        
        verify(restaurantGateway).findById(nonExistingId);
    }
} 