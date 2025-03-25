package fiap.restaurant.app.adapter.database.jpa.repository;

import fiap.restaurant.app.adapter.database.jpa.entity.RestaurantEntity;
import fiap.restaurant.app.core.domain.CuisineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, UUID> {
    List<RestaurantEntity> findByOwnerId(UUID ownerId);
    List<RestaurantEntity> findByNameContainingIgnoreCase(String name);
    List<RestaurantEntity> findByCuisineType(CuisineType cuisineType);
} 