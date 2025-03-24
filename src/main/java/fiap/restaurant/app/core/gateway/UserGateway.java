package fiap.restaurant.app.core.gateway;


import fiap.restaurant.app.core.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserGateway {
    User create(User user);

    void update(User user);

    void delete(UUID id);

    List<User> findAll();

    Optional<User> findById(UUID id);

    Optional<User> findByLogin(String login);

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);
}
