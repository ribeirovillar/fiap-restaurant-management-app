package fiap.restaurant.app.adapter.database.jpa;

import fiap.restaurant.app.adapter.database.jpa.entity.UserTypeEntity;
import fiap.restaurant.app.adapter.database.jpa.repository.UserTypeRepository;
import fiap.restaurant.app.adapter.presenter.UserTypePresenter;
import fiap.restaurant.app.core.domain.UserType;
import fiap.restaurant.app.core.gateway.UserTypeGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class UserTypeJpaGateway implements UserTypeGateway {

    private final UserTypeRepository repository;
    private final UserTypePresenter presenter;

    @Autowired
    public UserTypeJpaGateway(UserTypeRepository repository, UserTypePresenter presenter) {
        this.repository = repository;
        this.presenter = presenter;
        log.info("UserTypeJpaGateway created with presenter: {}", presenter.getClass().getName());
    }

    @Override
    public UserType create(UserType userType) {
        log.info("Creating user type: {}", userType.getName());
        UserTypeEntity userTypeEntity = presenter.mapToEntity(userType);
        userTypeEntity = repository.save(userTypeEntity);
        log.info("Created user type with ID: {}", userTypeEntity.getId());
        return presenter.mapToDomain(userTypeEntity);
    }

    @Override
    public void update(UserType userType) {
        log.info("Updating user type with ID: {}", userType.getId());
        
        Optional<UserTypeEntity> existingEntity = repository.findById(userType.getId());
        if (existingEntity.isEmpty()) {
            log.warn("User type with ID {} not found for update", userType.getId());
            return;
        }
        
        UserTypeEntity userTypeEntity = presenter.mapToEntity(userType);
        repository.save(userTypeEntity);
        log.info("Updated user type with ID: {}", userTypeEntity.getId());
    }

    @Override
    public void delete(UUID id) {
        log.info("Deleting user type with ID: {}", id);
        
        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("User type with ID {} deleted successfully", id);
        } else {
            log.warn("User type with ID {} not found for deletion", id);
        }
    }

    @Override
    public List<UserType> findAll() {
        return repository.findAll().stream()
                .map(presenter::mapToDomain)
                .toList();
    }

    @Override
    public Optional<UserType> findById(UUID id) {
        return repository.findById(id)
                .map(presenter::mapToDomain);
    }

    @Override
    public Optional<UserType> findByName(String name) {
        log.info("Finding user type by name: {}", name);
        return repository.findByName(name)
                .map(presenter::mapToDomain);
    }

    @Override
    public boolean existsByName(String name) {
        log.info("Checking if user type exists by name: {}", name);
        return repository.existsByName(name);
    }
} 