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
class FindRestaurantsByNameUseCaseImplTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    private FindRestaurantsByNameUseCaseImpl findRestaurantsByNameUseCase;

    @BeforeEach
    void setUp() {
        findRestaurantsByNameUseCase = new FindRestaurantsByNameUseCaseImpl(restaurantGateway);
    }

    @Test
    void shouldReturnRestaurantsMatchingName() {
        String searchName = "Pizza";
        
        User owner = User.builder()
                .id(UUID.randomUUID())
                .name("Restaurant Owner")
                .userType(UserTypeTestHelper.createOwnerDomain())
                .build();
        
        Restaurant restaurant1 = Restaurant.builder()
                .id(UUID.randomUUID())
                .name("Pizza Place")
                .cuisineType(CuisineType.ITALIAN)
                .owner(owner)
                .build();
                
        Restaurant restaurant2 = Restaurant.builder()
                .id(UUID.randomUUID())
                .name("Best Pizza in Town")
                .cuisineType(CuisineType.ITALIAN)
                .owner(owner)
                .build();
        
        List<Restaurant> expectedRestaurants = Arrays.asList(restaurant1, restaurant2);
        
        when(restaurantGateway.findByName(searchName)).thenReturn(expectedRestaurants);
        
        List<Restaurant> result = findRestaurantsByNameUseCase.execute(searchName);
        
        assertNotNull(result);
        assertEquals(2, result.size());
        
        for (Restaurant restaurant : result) {
            assertTrue(restaurant.getName().contains(searchName));
        }
        
        verify(restaurantGateway).findByName(searchName);
    }
    
    @Test
    void shouldReturnEmptyListWhenNoRestaurantsMatchName() {
        String searchName = "NonExistentName";
        
        when(restaurantGateway.findByName(searchName)).thenReturn(Collections.emptyList());
        
        List<Restaurant> result = findRestaurantsByNameUseCase.execute(searchName);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(restaurantGateway).findByName(searchName);
    }
} 