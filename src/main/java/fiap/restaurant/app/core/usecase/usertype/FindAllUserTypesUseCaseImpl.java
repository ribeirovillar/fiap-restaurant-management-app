package fiap.restaurant.app.core.usecase.usertype;

import fiap.restaurant.app.core.domain.UserType;
import fiap.restaurant.app.core.gateway.UserTypeGateway;

import java.util.List;

public class FindAllUserTypesUseCaseImpl implements FindAllUserTypesUseCase {

    private final UserTypeGateway userTypeGateway;

    public FindAllUserTypesUseCaseImpl(UserTypeGateway userTypeGateway) {
        this.userTypeGateway = userTypeGateway;
    }

    public List<UserType> execute() {
        return userTypeGateway.findAll();
    }
} 