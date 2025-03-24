package fiap.restaurant.app.config;

import fiap.restaurant.app.adapter.database.jpa.UserJpaGateway;
import fiap.restaurant.app.adapter.database.jpa.repository.UserRepository;
import fiap.restaurant.app.adapter.presenter.UserPresenterImpl;
import fiap.restaurant.app.core.controller.UserController;
import fiap.restaurant.app.core.gateway.UserGateway;
import fiap.restaurant.app.core.usecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    private final UserRepository userRepository;
    private final UserPresenterImpl userPresenter;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public UserGateway userGateway() {
        return new UserJpaGateway(userRepository, userPresenter);
    }

    @Bean
    public CreateUserUseCase createUserUseCase() {
        return new CreateUserUseCaseImpl(userGateway(), passwordEncoder);
    }

    @Bean
    public FindAllUsersUseCase findAllUsersUseCase() {
        return new FindAllUsersUseCaseImpl(userGateway());
    }

    @Bean
    public FindUserByIdUseCase findUserByIdUseCase() {
        return new FindUserByIdUseCaseImpl(userGateway());
    }

    @Bean
    public UpdateUserUseCase updateUserUseCase() {
        return new UpdateUserUseCaseImpl(userGateway(), findUserByIdUseCase());
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase() {
        return new DeleteUserUseCaseImpl(userGateway(), findUserByIdUseCase());
    }

    @Bean
    public ValidateLoginUseCase validateLoginUseCase() {
        return new ValidateLoginUseCaseImpl(userGateway(), passwordEncoder);
    }

    @Bean
    public UpdatePasswordUseCase updatePasswordUseCase() {
        return new UpdatePasswordUseCaseImpl(userGateway(), passwordEncoder, validateLoginUseCase());
    }

    @Bean
    public UserController userController() {
        return new UserController(
                createUserUseCase(),
                findAllUsersUseCase(),
                findUserByIdUseCase(),
                updateUserUseCase(),
                deleteUserUseCase(),
                validateLoginUseCase(),
                updatePasswordUseCase()
        );
    }
}
