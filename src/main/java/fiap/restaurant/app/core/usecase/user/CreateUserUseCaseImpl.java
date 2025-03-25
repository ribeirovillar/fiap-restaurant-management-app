package fiap.restaurant.app.core.usecase.user;

import fiap.restaurant.app.core.domain.User;
import fiap.restaurant.app.core.exception.DuplicatedDataException;
import fiap.restaurant.app.core.gateway.UserGateway;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;

    public CreateUserUseCaseImpl(UserGateway userGateway, PasswordEncoder passwordEncoder) {
        this.userGateway = userGateway;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User execute(User user) {
        user.validateForCreation();

        if (userGateway.existsByLogin(user.getLogin())) {
            throw new DuplicatedDataException("Login already exists: " + user.getLogin());
        }

        if (userGateway.existsByEmail(user.getEmail())) {
            throw new DuplicatedDataException("Email already exists: " + user.getEmail());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userGateway.create(user);
    }
}
