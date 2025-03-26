package fiap.restaurant.app.adapter.database.jpa.repository;

import fiap.restaurant.app.adapter.database.jpa.entity.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemEntity, UUID> {
    List<MenuItemEntity> findByRestaurantId(UUID restaurantId);
    MenuItemEntity findByIdAndRestaurantId(UUID id, UUID restaurantId);
} 