package fiap.restaurant.app.core.controller;

import fiap.restaurant.app.core.domain.Restaurant;
import fiap.restaurant.app.core.domain.User;
import fiap.restaurant.app.core.usecase.restaurant.*;
import fiap.restaurant.app.core.usecase.user.FindUserByIdUseCase;
import fiap.restaurant.app.core.usecase.user.FindUserByLoginUseCase;

import java.util.List;
import java.util.UUID;

public class RestaurantController {
    
    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final FindRestaurantByIdUseCase findRestaurantByIdUseCase;
    private final FindAllRestaurantsUseCase findAllRestaurantsUseCase;
    private final FindRestaurantsByOwnerIdUseCase findRestaurantsByOwnerIdUseCase;
    private final FindRestaurantsByNameUseCase findRestaurantsByNameUseCase;
    private final FindRestaurantsByCuisineTypeUseCase findRestaurantsByCuisineTypeUseCase;
    private final UpdateRestaurantUseCase updateRestaurantUseCase;
    private final DeleteRestaurantUseCase deleteRestaurantUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final ValidateRestaurantOwnerUseCase validateRestaurantOwnerUseCase;

    public RestaurantController(
            CreateRestaurantUseCase createRestaurantUseCase,
            FindRestaurantByIdUseCase findRestaurantByIdUseCase,
            FindAllRestaurantsUseCase findAllRestaurantsUseCase,
            FindRestaurantsByOwnerIdUseCase findRestaurantsByOwnerIdUseCase,
            FindRestaurantsByNameUseCase findRestaurantsByNameUseCase,
            FindRestaurantsByCuisineTypeUseCase findRestaurantsByCuisineTypeUseCase,
            UpdateRestaurantUseCase updateRestaurantUseCase,
            DeleteRestaurantUseCase deleteRestaurantUseCase,
            FindUserByIdUseCase findUserByIdUseCase,
            ValidateRestaurantOwnerUseCase validateRestaurantOwnerUseCase) {
        this.createRestaurantUseCase = createRestaurantUseCase;
        this.findRestaurantByIdUseCase = findRestaurantByIdUseCase;
        this.findAllRestaurantsUseCase = findAllRestaurantsUseCase;
        this.findRestaurantsByOwnerIdUseCase = findRestaurantsByOwnerIdUseCase;
        this.findRestaurantsByNameUseCase = findRestaurantsByNameUseCase;
        this.findRestaurantsByCuisineTypeUseCase = findRestaurantsByCuisineTypeUseCase;
        this.updateRestaurantUseCase = updateRestaurantUseCase;
        this.deleteRestaurantUseCase = deleteRestaurantUseCase;
        this.findUserByIdUseCase = findUserByIdUseCase;
        this.validateRestaurantOwnerUseCase = validateRestaurantOwnerUseCase;
    }

    public Restaurant create(Restaurant restaurant) {
        validateRestaurantOwnerUseCase.validateForCreation(restaurant.getOwner());
        restaurant.validateForCreation();
        return createRestaurantUseCase.execute(restaurant);
    }
    
    public User findUserById(UUID id) {
        return findUserByIdUseCase.execute(id);
    }

    public Restaurant findById(UUID id) {
        return findRestaurantByIdUseCase.execute(id);
    }

    public List<Restaurant> findAll() {
        return findAllRestaurantsUseCase.execute();
    }

    public List<Restaurant> findByOwnerId(UUID ownerId) {
        return findRestaurantsByOwnerIdUseCase.execute(ownerId);
    }

    public List<Restaurant> findByName(String name) {
        return findRestaurantsByNameUseCase.execute(name);
    }

    public List<Restaurant> findByCuisineType(String cuisineType) {
        return findRestaurantsByCuisineTypeUseCase.execute(cuisineType);
    }

    public Restaurant update(Restaurant restaurant) {
        Restaurant existingRestaurant = findById(restaurant.getId());
        validateRestaurantOwnerUseCase.validateForUpdate(restaurant.getOwner(), existingRestaurant.getOwner());
        restaurant.validateForUpdate(existingRestaurant);
        return updateRestaurantUseCase.execute(restaurant);
    }

    public void delete(UUID id) {
        Restaurant restaurant = findById(id);
        validateRestaurantOwnerUseCase.validateForDelete(restaurant.getOwner());
        restaurant.validateForDelete();
        deleteRestaurantUseCase.execute(id);
    }
} 