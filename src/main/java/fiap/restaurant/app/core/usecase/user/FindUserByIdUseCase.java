package fiap.restaurant.app.core.usecase.user;

import fiap.restaurant.app.core.domain.User;

import java.util.UUID;

public interface FindUserByIdUseCase {
    User execute(UUID id);
} 