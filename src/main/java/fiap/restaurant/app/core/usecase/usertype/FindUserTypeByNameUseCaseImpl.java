package fiap.restaurant.app.core.usecase.usertype;

import fiap.restaurant.app.core.domain.UserType;
import fiap.restaurant.app.core.exception.BusinessException;
import fiap.restaurant.app.core.gateway.UserTypeGateway;

public class FindUserTypeByNameUseCaseImpl implements FindUserTypeByNameUseCase {

    private final UserTypeGateway userTypeGateway;

    public FindUserTypeByNameUseCaseImpl(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    @Override
    public UserType execute(String name) {
        return userTypeGateway.findByName(name)
                .orElseThrow(() ->
                        new BusinessException("User type with name " + name + " not found")
                );
    }
} 