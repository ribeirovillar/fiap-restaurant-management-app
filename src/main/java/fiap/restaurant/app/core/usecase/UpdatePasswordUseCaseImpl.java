package fiap.restaurant.app.core.usecase;

import fiap.restaurant.app.core.domain.User;
import fiap.restaurant.app.core.exception.InvalidCredentialException;
import fiap.restaurant.app.core.gateway.UserGateway;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UpdatePasswordUseCaseImpl implements UpdatePasswordUseCase {

    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;
    private final ValidateLoginUseCase validateLoginUseCase;

    public UpdatePasswordUseCaseImpl(UserGateway userGateway, 
                                    PasswordEncoder passwordEncoder,
                                    ValidateLoginUseCase validateLoginUseCase) {
        this.userGateway = userGateway;
        this.passwordEncoder = passwordEncoder;
        this.validateLoginUseCase = validateLoginUseCase;
    }

    @Override
    public void execute(String login, String currentPassword, String newPassword) {
        try {
            validateLoginUseCase.execute(login, currentPassword);
        } catch (InvalidCredentialException e) {
            throw new InvalidCredentialException("Current password is incorrect");
        }
        
        User user = userGateway.findByLogin(login)
                .orElseThrow(() -> new InvalidCredentialException("User not found"));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        
        userGateway.update(user);
    }
} 