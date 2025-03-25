package fiap.restaurant.app.core.gateway;

import fiap.restaurant.app.core.domain.Restaurant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantGateway {
    Restaurant create(Restaurant restaurant);
    Optional<Restaurant> findById(UUID id);
    List<Restaurant> findAll();
    List<Restaurant> findByOwnerId(UUID ownerId);
    List<Restaurant> findByName(String name);
    List<Restaurant> findByCuisineType(String cuisineType);
    Restaurant update(Restaurant restaurant);
    void deleteById(UUID id);
} 