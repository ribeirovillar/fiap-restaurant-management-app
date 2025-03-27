package fiap.restaurant.app.configuration;

import fiap.restaurant.app.adapter.database.jpa.UserTypeJpaGateway;
import fiap.restaurant.app.adapter.database.jpa.repository.UserTypeRepository;
import fiap.restaurant.app.adapter.presenter.UserTypePresenter;
import fiap.restaurant.app.core.controller.UserTypeController;
import fiap.restaurant.app.core.gateway.UserTypeGateway;
import fiap.restaurant.app.core.usecase.usertype.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UserTypeBeanConfiguration {

    private final UserTypeRepository userTypeRepository;
    private final UserTypePresenter userTypePresenter;

    @Bean
    public UserTypeGateway userTypeGateway() {
        return new UserTypeJpaGateway(userTypeRepository, userTypePresenter);
    }

    @Bean
    public CreateUserTypeUseCase createUserTypeUseCase() {
        return new CreateUserTypeUseCaseImpl(userTypeGateway());
    }

    @Bean
    public FindAllUserTypesUseCase findAllUserTypesUseCase() {
        return new FindAllUserTypesUseCaseImpl(userTypeGateway());
    }

    @Bean
    public FindUserTypeByIdUseCase findUserTypeByIdUseCase() {
        return new FindUserTypeByIdUseCaseImpl(userTypeGateway());
    }

    @Bean
    public FindUserTypeByNameUseCase findUserTypeByNameUseCase() {
        return new FindUserTypeByNameUseCaseImpl(userTypeGateway());
    }

    @Bean
    public UpdateUserTypeUseCase updateUserTypeUseCase() {
        return new UpdateUserTypeUseCaseImpl(userTypeGateway(), findUserTypeByIdUseCase());
    }

    @Bean
    public DeleteUserTypeUseCase deleteUserTypeUseCase() {
        return new DeleteUserTypeUseCaseImpl(userTypeGateway(), findUserTypeByIdUseCase());
    }

    @Bean
    public UserTypeController userTypeController() {
        return new UserTypeController(
                createUserTypeUseCase(),
                findAllUserTypesUseCase(),
                findUserTypeByIdUseCase(),
                findUserTypeByNameUseCase(),
                updateUserTypeUseCase(),
                deleteUserTypeUseCase()
        );
    }
} 