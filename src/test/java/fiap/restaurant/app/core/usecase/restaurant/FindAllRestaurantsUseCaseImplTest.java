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
class FindAllRestaurantsUseCaseImplTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    private FindAllRestaurantsUseCaseImpl findAllRestaurantsUseCase;

    @BeforeEach
    void setUp() {
        findAllRestaurantsUseCase = new FindAllRestaurantsUseCaseImpl(restaurantGateway);
    }

    @Test
    void shouldReturnAllRestaurants() {
        UUID ownerId1 = UUID.randomUUID();
        User owner1 = User.builder()
                .id(ownerId1)
                .name("Owner 1")
                .userType(UserType.OWNER)
                .build();
                
        UUID ownerId2 = UUID.randomUUID();
        User owner2 = User.builder()
                .id(ownerId2)
                .name("Owner 2")
                .userType(UserType.OWNER)
                .build();
        
        Restaurant restaurant1 = Restaurant.builder()
                .id(UUID.randomUUID())
                .name("Restaurant 1")
                .cuisineType(CuisineType.ITALIAN)
                .owner(owner1)
                .build();
                
        Restaurant restaurant2 = Restaurant.builder()
                .id(UUID.randomUUID())
                .name("Restaurant 2")
                .cuisineType(CuisineType.MEXICAN)
                .owner(owner2)
                .build();
        
        List<Restaurant> expectedRestaurants = Arrays.asList(restaurant1, restaurant2);
        
        when(restaurantGateway.findAll()).thenReturn(expectedRestaurants);
        
        List<Restaurant> result = findAllRestaurantsUseCase.execute();
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedRestaurants, result);
        
        verify(restaurantGateway).findAll();
    }
    
    @Test
    void shouldReturnEmptyListWhenNoRestaurantsExist() {
        when(restaurantGateway.findAll()).thenReturn(Collections.emptyList());
        
        List<Restaurant> result = findAllRestaurantsUseCase.execute();
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(restaurantGateway).findAll();
    }
} 