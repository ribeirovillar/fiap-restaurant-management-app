package fiap.restaurant.app.core.usecase;

import fiap.restaurant.app.core.domain.User;

import java.util.UUID;

public interface UpdateUserUseCase {
    User execute(UUID id, User userUpdate);
} 