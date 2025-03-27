package fiap.restaurant.app.core.usecase.restaurant;

import fiap.restaurant.app.core.domain.*;
import fiap.restaurant.app.core.exception.UnauthorizedOperationException;
import fiap.restaurant.app.core.gateway.RestaurantGateway;
import fiap.restaurant.app.util.UserTypeTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantUseCaseImplTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    private CreateRestaurantUseCaseImpl createRestaurantUseCase;

    @BeforeEach
    void setUp() {
        createRestaurantUseCase = new CreateRestaurantUseCaseImpl(restaurantGateway);
    }

    @Test
    void shouldCreateRestaurantSuccessfully() {
        UUID restaurantId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        UserType ownerType = UserTypeTestHelper.createOwnerDomain();
        
        User owner = User.builder()
                .id(ownerId)
                .name("Restaurant Owner")
                .email("owner@example.com")
                .login("owner123")
                .password("password123")
                .userType(ownerType)
                .build();
        
        List<BusinessHours> businessHours = new ArrayList<>();
        businessHours.add(BusinessHours.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .openingTime(LocalTime.of(9, 0))
                .closingTime(LocalTime.of(18, 0))
                .isClosed(false)
                .build());
        
        Restaurant restaurant = Restaurant.builder()
                .name("Test Restaurant")
                .cuisineType(CuisineType.ITALIAN)
                .owner(owner)
                .businessHours(businessHours)
                .build();
        
        Restaurant createdRestaurant = Restaurant.builder()
                .id(restaurantId)
                .name("Test Restaurant")
                .cuisineType(CuisineType.ITALIAN)
                .owner(owner)
                .businessHours(businessHours)
                .createdAt("2023-01-01T12:00:00")
                .updatedAt("2023-01-01T12:00:00")
                .build();
        
        when(restaurantGateway.create(any(Restaurant.class))).thenReturn(createdRestaurant);
        
        Restaurant result = createRestaurantUseCase.execute(restaurant);
        
        assertNotNull(result);
        assertEquals(restaurantId, result.getId());
        assertEquals("Test Restaurant", result.getName());
        assertEquals(CuisineType.ITALIAN, result.getCuisineType());
        assertEquals(ownerId, result.getOwner().getId());
        assertEquals(1, result.getBusinessHours().size());
        assertEquals("2023-01-01T12:00:00", result.getCreatedAt());
        assertEquals("2023-01-01T12:00:00", result.getUpdatedAt());
        
        verify(restaurantGateway).create(any(Restaurant.class));
    }
    
    @Test
    void shouldThrowExceptionWhenRestaurantNameIsInvalid() {
        UserType ownerType = UserTypeTestHelper.createOwnerDomain();
        
        User owner = User.builder()
                .id(UUID.randomUUID())
                .name("Restaurant Owner")
                .userType(ownerType)
                .build();
        
        Restaurant restaurant = Restaurant.builder()
                .name("AB")
                .cuisineType(CuisineType.ITALIAN)
                .owner(owner)
                .businessHours(List.of(BusinessHours.builder()
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .openingTime(LocalTime.of(9, 0))
                        .closingTime(LocalTime.of(18, 0))
                        .isClosed(false)
                        .build()))
                .build();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> createRestaurantUseCase.execute(restaurant));
        
        assertEquals("Restaurant name must be at least 3 characters long", exception.getMessage());
        verify(restaurantGateway, never()).create(any(Restaurant.class));
    }
    
    @Test
    void shouldThrowExceptionWhenOwnerIsNotOwnerType() {
        UserType customerType = UserTypeTestHelper.createCustomerDomain();
        
        User owner = User.builder()
                .id(UUID.randomUUID())
                .name("Customer User")
                .userType(customerType)
                .build();
        
        Restaurant restaurant = Restaurant.builder()
                .name("Test Restaurant")
                .cuisineType(CuisineType.ITALIAN)
                .owner(owner)
                .businessHours(List.of(BusinessHours.builder()
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .openingTime(LocalTime.of(9, 0))
                        .closingTime(LocalTime.of(18, 0))
                        .isClosed(false)
                        .build()))
                .build();
        
        UnauthorizedOperationException exception = assertThrows(UnauthorizedOperationException.class, 
                () -> createRestaurantUseCase.execute(restaurant));
        
        assertEquals("Only OWNER users can create restaurants", exception.getMessage());
        verify(restaurantGateway, never()).create(any(Restaurant.class));
    }
    
    @Test
    void shouldThrowExceptionWhenBusinessHoursAreInvalid() {
        UserType ownerType = UserTypeTestHelper.createOwnerDomain();
        
        User owner = User.builder()
                .id(UUID.randomUUID())
                .name("Restaurant Owner")
                .userType(ownerType)
                .build();
        
        Restaurant restaurant = Restaurant.builder()
                .name("Test Restaurant")
                .cuisineType(CuisineType.ITALIAN)
                .owner(owner)
                .businessHours(List.of(BusinessHours.builder()
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .openingTime(LocalTime.of(18, 0))
                        .closingTime(LocalTime.of(9, 0))
                        .isClosed(false)
                        .build()))
                .build();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> createRestaurantUseCase.execute(restaurant));
        
        assertEquals("Closing time cannot be before opening time", exception.getMessage());
        verify(restaurantGateway, never()).create(any(Restaurant.class));
    }
    
    @Test
    void shouldThrowExceptionWhenRequiredFieldsAreMissing() {
        UserType ownerType = UserTypeTestHelper.createOwnerDomain();
        
        User owner = User.builder()
                .id(UUID.randomUUID())
                .name("Restaurant Owner")
                .userType(ownerType)
                .build();
        
        Restaurant restaurant = Restaurant.builder()
                .name("Test Restaurant")
                .owner(owner)
                .businessHours(List.of(BusinessHours.builder()
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .openingTime(LocalTime.of(9, 0))
                        .closingTime(LocalTime.of(18, 0))
                        .isClosed(false)
                        .build()))
                .build();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> createRestaurantUseCase.execute(restaurant));
        
        assertEquals("Cuisine type is required", exception.getMessage());
        verify(restaurantGateway, never()).create(any(Restaurant.class));
    }
} 