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
class FindRestaurantsByCuisineTypeUseCaseImplTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    private FindRestaurantsByCuisineTypeUseCaseImpl findRestaurantsByCuisineTypeUseCase;

    @BeforeEach
    void setUp() {
        findRestaurantsByCuisineTypeUseCase = new FindRestaurantsByCuisineTypeUseCaseImpl(restaurantGateway);
    }

    @Test
    void shouldReturnRestaurantsMatchingCuisineType() {
        String cuisineType = CuisineType.ITALIAN.name();
        
        User owner = User.builder()
                .id(UUID.randomUUID())
                .name("Restaurant Owner")
                .userType(UserTypeTestHelper.createOwnerDomain())
                .build();
        
        Restaurant restaurant1 = Restaurant.builder()
                .id(UUID.randomUUID())
                .name("Italian Restaurant 1")
                .cuisineType(CuisineType.ITALIAN)
                .owner(owner)
                .build();
                
        Restaurant restaurant2 = Restaurant.builder()
                .id(UUID.randomUUID())
                .name("Italian Restaurant 2")
                .cuisineType(CuisineType.ITALIAN)
                .owner(owner)
                .build();
        
        List<Restaurant> expectedRestaurants = Arrays.asList(restaurant1, restaurant2);
        
        when(restaurantGateway.findByCuisineType(cuisineType)).thenReturn(expectedRestaurants);
        
        List<Restaurant> result = findRestaurantsByCuisineTypeUseCase.execute(cuisineType);
        
        assertNotNull(result);
        assertEquals(2, result.size());
        
        for (Restaurant restaurant : result) {
            assertEquals(CuisineType.ITALIAN, restaurant.getCuisineType());
        }
        
        verify(restaurantGateway).findByCuisineType(cuisineType);
    }
    
    @Test
    void shouldReturnEmptyListWhenNoRestaurantsMatchCuisineType() {
        String cuisineType = CuisineType.JAPANESE.name();
        
        when(restaurantGateway.findByCuisineType(cuisineType)).thenReturn(Collections.emptyList());
        
        List<Restaurant> result = findRestaurantsByCuisineTypeUseCase.execute(cuisineType);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(restaurantGateway).findByCuisineType(cuisineType);
    }
} 