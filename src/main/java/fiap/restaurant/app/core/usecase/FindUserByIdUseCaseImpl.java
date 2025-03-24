package fiap.restaurant.app.core.usecase;

import fiap.restaurant.app.core.domain.User;
import fiap.restaurant.app.core.exception.UserNotFoundException;
import fiap.restaurant.app.core.gateway.UserGateway;

import java.util.UUID;

public class FindUserByIdUseCaseImpl implements FindUserByIdUseCase {

    private final UserGateway userGateway;

    public FindUserByIdUseCaseImpl(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public User execute(UUID id) {
        return userGateway.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }
} 