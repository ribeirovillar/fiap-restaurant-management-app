package fiap.restaurant.app.core.controller;

import fiap.restaurant.app.core.domain.Restaurant;
import fiap.restaurant.app.core.domain.User;
import fiap.restaurant.app.core.usecase.restaurant.*;
import fiap.restaurant.app.core.usecase.user.FindUserByIdUseCase;

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

    public RestaurantController(
            CreateRestaurantUseCase createRestaurantUseCase,
            FindRestaurantByIdUseCase findRestaurantByIdUseCase,
            FindAllRestaurantsUseCase findAllRestaurantsUseCase,
            FindRestaurantsByOwnerIdUseCase findRestaurantsByOwnerIdUseCase,
            FindRestaurantsByNameUseCase findRestaurantsByNameUseCase,
            FindRestaurantsByCuisineTypeUseCase findRestaurantsByCuisineTypeUseCase,
            UpdateRestaurantUseCase updateRestaurantUseCase,
            DeleteRestaurantUseCase deleteRestaurantUseCase,
            FindUserByIdUseCase findUserByIdUseCase) {
        this.createRestaurantUseCase = createRestaurantUseCase;
        this.findRestaurantByIdUseCase = findRestaurantByIdUseCase;
        this.findAllRestaurantsUseCase = findAllRestaurantsUseCase;
        this.findRestaurantsByOwnerIdUseCase = findRestaurantsByOwnerIdUseCase;
        this.findRestaurantsByNameUseCase = findRestaurantsByNameUseCase;
        this.findRestaurantsByCuisineTypeUseCase = findRestaurantsByCuisineTypeUseCase;
        this.updateRestaurantUseCase = updateRestaurantUseCase;
        this.deleteRestaurantUseCase = deleteRestaurantUseCase;
        this.findUserByIdUseCase = findUserByIdUseCase;
    }

    public Restaurant create(Restaurant restaurant) {
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

    public Restaurant update(UUID id, Restaurant restaurant) {
        return updateRestaurantUseCase.execute(id, restaurant);
    }

    public void delete(UUID id) {
        deleteRestaurantUseCase.execute(id);
    }
} 