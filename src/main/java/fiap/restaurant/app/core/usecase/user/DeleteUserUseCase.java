package fiap.restaurant.app.core.usecase.user;

import java.util.UUID;

public interface DeleteUserUseCase {
    void execute(UUID id);
} 