package fiap.restaurant.app.core.usecase.user;

import fiap.restaurant.app.core.domain.User;

public interface CreateUserUseCase {
    User execute(User user);
}
