package fiap.restaurant.app.core.usecase.restaurant;

import fiap.restaurant.app.core.domain.CuisineType;
import fiap.restaurant.app.core.domain.Restaurant;
import fiap.restaurant.app.core.domain.User;
import fiap.restaurant.app.core.gateway.RestaurantGateway;
import fiap.restaurant.app.util.UserTypeTestHelper;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        UUID ownerId = UUID.randomUUID();
        
        User owner = User.builder()
                .id(ownerId)
                .name("Restaurant Owner")
                .userType(UserTypeTestHelper.createOwnerDomain())
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
        
        List<Restaurant> result = findRestaurantsByOwnerIdUseCase.execute(ownerId);
        
        assertNotNull(result);
        assertEquals(2, result.size());
        
        for (Restaurant restaurant : result) {
            assertEquals(ownerId, restaurant.getOwner().getId());
        }
        
        verify(restaurantGateway).findByOwnerId(ownerId);
    }
    
    @Test
    void shouldReturnEmptyListWhenOwnerHasNoRestaurants() {
        UUID ownerId = UUID.randomUUID();
        
        when(restaurantGateway.findByOwnerId(ownerId)).thenReturn(Collections.emptyList());
        
        List<Restaurant> result = findRestaurantsByOwnerIdUseCase.execute(ownerId);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(restaurantGateway).findByOwnerId(ownerId);
    }
} 