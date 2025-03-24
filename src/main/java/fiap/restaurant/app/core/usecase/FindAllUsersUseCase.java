package fiap.restaurant.app.core.usecase;

import fiap.restaurant.app.core.domain.User;

import java.util.List;

public interface FindAllUsersUseCase {
    List<User> execute();
} 