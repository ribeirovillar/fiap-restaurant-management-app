package fiap.restaurant.app.adapter.database.jpa;

import fiap.restaurant.app.adapter.database.jpa.entity.UserEntity;
import fiap.restaurant.app.adapter.database.jpa.repository.UserRepository;
import fiap.restaurant.app.adapter.presenter.UserPresenter;
import fiap.restaurant.app.core.domain.User;
import fiap.restaurant.app.core.gateway.UserGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class UserJpaGateway implements UserGateway {

    private final UserRepository repository;
    private final UserPresenter presenter;

    @Autowired
    public UserJpaGateway(UserRepository repository, @Qualifier("userPresenterImpl") UserPresenter presenter) {
        this.repository = repository;
        this.presenter = presenter;
        log.info("UserJpaGateway created with presenter: {}", presenter.getClass().getName());
    }

    @Override
    public User create(User user) {
        log.info("Creating user: {}", user.getName());
        UserEntity userEntity = presenter.mapToEntity(user);

        if (userEntity.getAddress() != null) {
            userEntity.getAddress().setUser(userEntity);
        }

        userEntity = repository.save(userEntity);
        log.info("Created user with ID: {}", userEntity.getId());

        return presenter.mapToDomain(userEntity);
    }

    @Override
    public void update(User user) {
        log.info("Updating user with ID: {}", user.getId());

        Optional<UserEntity> existingEntity = repository.findById(user.getId());
        if (existingEntity.isEmpty()) {
            log.warn("User with ID {} not found for update", user.getId());
            return;
        }

        UserEntity userEntity = presenter.mapToEntity(user);

        userEntity.setLastModifiedDate(LocalDateTime.now());

        if (userEntity.getAddress() != null) {
            userEntity.getAddress().setUser(userEntity);
        }

        repository.save(userEntity);
        log.info("Updated user with ID: {}", userEntity.getId());
    }

    @Override
    public void delete(UUID id) {
        log.info("Deleting user with ID: {}", id);
        
        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("User with ID {} deleted successfully", id);
        } else {
            log.warn("User with ID {} not found for deletion", id);
        }
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream()
                .map(presenter::mapToDomain)
                .toList();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id)
                .map(presenter::mapToDomain);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        log.info("Finding user by login: {}", login);
        return repository.findByLogin(login)
                .map(presenter::mapToDomain);
    }

    @Override
    public boolean existsByLogin(String login) {
        log.info("Checking if login exists: {}", login);
        return repository.existsByLogin(login);
    }

    @Override
    public boolean existsByEmail(String email) {
        log.info("Checking if email exists: {}", email);
        return repository.existsByEmail(email);
    }
}
