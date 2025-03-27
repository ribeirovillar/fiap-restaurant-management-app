package fiap.restaurant.app.core.gateway;

import fiap.restaurant.app.core.domain.UserType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserTypeGateway {
    UserType create(UserType userType);

    void update(UserType userType);

    void delete(UUID id);

    List<UserType> findAll();

    Optional<UserType> findById(UUID id);

    Optional<UserType> findByName(String name);

    boolean existsByName(String name);
} 