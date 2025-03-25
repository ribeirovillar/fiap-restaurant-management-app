package fiap.restaurant.app.core.usecase.user;

import fiap.restaurant.app.core.exception.InvalidCredentialException;
import fiap.restaurant.app.core.gateway.UserGateway;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ValidateLoginUseCaseImpl implements ValidateLoginUseCase {

    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;

    public ValidateLoginUseCaseImpl(UserGateway userGateway, PasswordEncoder passwordEncoder) {
        this.userGateway = userGateway;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void execute(String login, String password) {
        userGateway.findByLogin(login)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .filter(isValid -> isValid)
                .orElseThrow(() -> new InvalidCredentialException("Invalid login or password"));
    }
} 