package fiap.restaurant.app.core.usecase.user;

import fiap.restaurant.app.core.domain.User;
import fiap.restaurant.app.core.gateway.UserGateway;

import java.util.List;

public class FindAllUsersUseCaseImpl implements FindAllUsersUseCase {

    private final UserGateway userGateway;

    public FindAllUsersUseCaseImpl(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public List<User> execute() {
        return userGateway.findAll();
    }
} 