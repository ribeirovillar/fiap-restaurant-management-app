package fiap.restaurant.app.core.usecase.user;

import fiap.restaurant.app.core.domain.User;
import fiap.restaurant.app.core.exception.UserNotFoundException;
import fiap.restaurant.app.core.gateway.UserGateway;

public class FindUserByLoginUseCase {
    private final UserGateway userGateway;

    public FindUserByLoginUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User execute(String login) {
        return userGateway.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User not found with login: " + login));
    }
} 