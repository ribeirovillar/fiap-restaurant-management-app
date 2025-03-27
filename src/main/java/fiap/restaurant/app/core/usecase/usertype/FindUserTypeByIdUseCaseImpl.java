package fiap.restaurant.app.core.usecase.usertype;

import fiap.restaurant.app.core.domain.UserType;
import fiap.restaurant.app.core.exception.BusinessException;
import fiap.restaurant.app.core.gateway.UserTypeGateway;

import java.util.UUID;

public class FindUserTypeByIdUseCaseImpl implements FindUserTypeByIdUseCase {

    private final UserTypeGateway userTypeGateway;

    public FindUserTypeByIdUseCaseImpl(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    @Override
    public UserType execute(UUID id) {
        return userTypeGateway.findById(id)
                .orElseThrow(() ->
                        new BusinessException("User type with ID " + id + " not found")
                );
    }
} 