package fiap.restaurant.app.core.usecase.restaurant;

import fiap.restaurant.app.core.domain.CuisineType;
import fiap.restaurant.app.core.domain.Restaurant;
import fiap.restaurant.app.core.domain.User;
import fiap.restaurant.app.core.domain.UserType;
import fiap.restaurant.app.core.gateway.RestaurantGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindRestaurantsByOwnerIdUseCaseImplTest {

    @Mock
    private RestaurantGateway restaurantGateway;
    
    private FindRestaurantsByOwnerIdUseCaseImpl findRestaurantsByOwnerIdUseCase;
    
    @BeforeEach
    void setUp() {
        findRestaurantsByOwnerIdUseCase = new FindRestaurantsByOwnerIdUseCaseImpl(restaurantGateway);
    }
    
    @Test
    void shouldReturnRestaurantsForSpecificOwner() {
        // Given
        UUID ownerId = UUID.randomUUID();
        
        User owner = User.builder()
                .id(ownerId)
                .name("Restaurant Owner")
                .userType(UserType.OWNER)
                .build();
        
        Restaurant restaurant1 = Restaurant.builder()
                .id(UUID.randomUUID())
                .name("Owner's Restaurant 1")
                .cuisineType(CuisineType.ITALIAN)
                .owner(owner)
                .build();
                
        Restaurant restaurant2 = Restaurant.builder()
                .id(UUID.randomUUID())
                .name("Owner's Restaurant 2")
                .cuisineType(CuisineType.MEXICAN)
                .owner(owner)
                .build();
        
        List<Restaurant> expectedRestaurants = Arrays.asList(restaurant1, restaurant2);
        
        when(restaurantGateway.findByOwnerId(ownerId)).thenReturn(expectedRestaurants);
        
        // When
        List<Restaurant> result = findRestaurantsByOwnerIdUseCase.execute(ownerId);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        
        for (Restaurant restaurant : result) {
            assertEquals(ownerId, restaurant.getOwner().getId());
        }
        
        verify(restaurantGateway).findByOwnerId(ownerId);
    }
    
    @Test
    void shouldReturnEmptyListWhenOwnerHasNoRestaurants() {
        // Given
        UUID ownerId = UUID.randomUUID();
        
        when(restaurantGateway.findByOwnerId(ownerId)).thenReturn(Collections.emptyList());
        
        // When
        List<Restaurant> result = findRestaurantsByOwnerIdUseCase.execute(ownerId);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(restaurantGateway).findByOwnerId(ownerId);
    }
} 