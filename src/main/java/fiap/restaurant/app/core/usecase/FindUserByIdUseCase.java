package fiap.restaurant.app.core.usecase;

import fiap.restaurant.app.core.domain.User;

import java.util.UUID;

public interface FindUserByIdUseCase {
    User execute(UUID id);
} 