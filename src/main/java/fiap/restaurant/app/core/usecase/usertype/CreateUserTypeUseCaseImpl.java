package fiap.restaurant.app.core.usecase.usertype;

import fiap.restaurant.app.core.domain.UserType;
import fiap.restaurant.app.core.exception.BusinessException;
import fiap.restaurant.app.core.gateway.UserTypeGateway;

public class CreateUserTypeUseCaseImpl implements CreateUserTypeUseCase {

    private final UserTypeGateway userTypeGateway;

    public CreateUserTypeUseCaseImpl(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public UserType execute(String name) {
        if (userTypeGateway.existsByName(name)) {
            throw new BusinessException("User type with name " + name + " already exists");
        }

        UserType userType = UserType.builder()
                .name(name)
                .build();

        return userTypeGateway.create(userType);
    }
} 