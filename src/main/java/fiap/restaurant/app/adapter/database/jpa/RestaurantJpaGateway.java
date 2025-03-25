package fiap.restaurant.app.adapter.database.jpa;

import fiap.restaurant.app.adapter.database.jpa.entity.RestaurantEntity;
import fiap.restaurant.app.adapter.presenter.RestaurantPresenter;
import fiap.restaurant.app.adapter.database.jpa.repository.RestaurantJpaRepository;
import fiap.restaurant.app.core.domain.CuisineType;
import fiap.restaurant.app.core.domain.Restaurant;
import fiap.restaurant.app.core.gateway.RestaurantGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantJpaGateway implements RestaurantGateway {

    private final RestaurantJpaRepository restaurantRepository;
    private final RestaurantPresenter restaurantPresenter;

    @Override
    @Transactional
    public Restaurant create(Restaurant restaurant) {
        log.info("Creating restaurant: {}", restaurant.getName());
        
        RestaurantEntity entity = restaurantPresenter.toEntity(restaurant, null);
        restaurantPresenter.updateBusinessHours(entity, restaurant.getBusinessHours());
        
        entity = restaurantRepository.save(entity);
        log.info("Created restaurant with ID: {}", entity.getId());
        
        return restaurantPresenter.toDomain(entity);
    }

    @Override
    public Optional<Restaurant> findById(UUID id) {
        log.info("Finding restaurant with ID: {}", id);
        
        return restaurantRepository.findById(id)
                .map(restaurantPresenter::toDomain);
    }

    @Override
    public List<Restaurant> findAll() {
        log.info("Finding all restaurants");
        
        return restaurantRepository.findAll()
                .stream()
                .map(restaurantPresenter::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Restaurant> findByOwnerId(UUID ownerId) {
        log.info("Finding restaurants by owner ID: {}", ownerId);
        
        return restaurantRepository.findByOwnerId(ownerId)
                .stream()
                .map(restaurantPresenter::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Restaurant> findByName(String name) {
        log.info("Finding restaurants by name: {}", name);
        
        return restaurantRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(restaurantPresenter::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Restaurant> findByCuisineType(String cuisineType) {
        log.info("Finding restaurants by cuisine type: {}", cuisineType);
        
        try {
            CuisineType type = CuisineType.valueOf(cuisineType.toUpperCase());
            return restaurantRepository.findByCuisineType(type)
                    .stream()
                    .map(restaurantPresenter::toDomain)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            log.error("Invalid cuisine type: {}", cuisineType);
            return List.of();
        }
    }

    @Override
    @Transactional
    public Restaurant update(Restaurant restaurant) {
        log.info("Updating restaurant with ID: {}", restaurant.getId());
        
        Optional<RestaurantEntity> existingEntity = restaurantRepository.findById(restaurant.getId());
        
        if (existingEntity.isPresent()) {
            RestaurantEntity entity = restaurantPresenter.toEntity(restaurant, existingEntity.get());
            restaurantPresenter.updateBusinessHours(entity, restaurant.getBusinessHours());
            
            entity = restaurantRepository.save(entity);
            log.info("Updated restaurant with ID: {}", entity.getId());
            
            return restaurantPresenter.toDomain(entity);
        } else {
            log.error("Restaurant with ID {} not found", restaurant.getId());
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        log.info("Deleting restaurant with ID: {}", id);
        
        if (restaurantRepository.existsById(id)) {
            restaurantRepository.deleteById(id);
            log.info("Restaurant with ID {} deleted successfully", id);
        } else {
            log.error("Restaurant with ID {} not found", id);
        }
    }
} 