package fiap.restaurant.app.core.usecase.user;

import fiap.restaurant.app.core.domain.User;

import java.util.List;

public interface FindAllUsersUseCase {
    List<User> execute();
} 