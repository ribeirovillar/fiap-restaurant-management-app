package fiap.restaurant.app.core.usecase;

import fiap.restaurant.app.core.domain.User;

public interface CreateUserUseCase {
    User execute(User user);
}
