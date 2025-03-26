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

    private FindAllRestaurantsUseCase findAllRestaurantsUseCase;

    @BeforeEach
    void setUp() {
        findAllRestaurantsUseCase = new FindAllRestaurantsUseCaseImpl(restaurantGateway);
    }

    @Test
    void execute_ShouldReturnAllRestaurants_WhenRestaurantsExist() {
        List<Restaurant> expectedRestaurants = createSampleRestaurants();
        when(restaurantGateway.findAll()).thenReturn(expectedRestaurants);
        
        List<Restaurant> result = findAllRestaurantsUseCase.execute();
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedRestaurants, result);
        verify(restaurantGateway).findAll();
    }
    
    @Test
    void execute_ShouldReturnEmptyList_WhenNoRestaurantsExist() {
        when(restaurantGateway.findAll()).thenReturn(Collections.emptyList());
        
        List<Restaurant> result = findAllRestaurantsUseCase.execute();
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(restaurantGateway).findAll();
    }
    
    private List<Restaurant> createSampleRestaurants() {
        User owner1 = createOwner("Owner 1");
        User owner2 = createOwner("Owner 2");
        
        Restaurant restaurant1 = createRestaurant(
                "Restaurant 1", 
                CuisineType.ITALIAN, 
                owner1
        );
                
        Restaurant restaurant2 = createRestaurant(
                "Restaurant 2", 
                CuisineType.MEXICAN, 
                owner2
        );
        
        return Arrays.asList(restaurant1, restaurant2);
    }
    
    private User createOwner(String name) {
        return User.builder()
                .id(UUID.randomUUID())
                .name(name)
                .userType(UserType.OWNER)
                .build();
    }
    
    private Restaurant createRestaurant(String name, CuisineType cuisineType, User owner) {
        return Restaurant.builder()
                .id(UUID.randomUUID())
                .name(name)
                .cuisineType(cuisineType)
                .owner(owner)
                .build();
    }
} 