package fiap.restaurant.app.adapter.database.jpa.repository;

import fiap.restaurant.app.adapter.database.jpa.entity.UserTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserTypeRepository extends JpaRepository<UserTypeEntity, UUID> {
    Optional<UserTypeEntity> findByName(String name);
    boolean existsByName(String name);
} 