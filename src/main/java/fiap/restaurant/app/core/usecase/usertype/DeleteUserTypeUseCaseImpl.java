package fiap.restaurant.app.core.usecase.usertype;

import fiap.restaurant.app.core.domain.UserType;
import fiap.restaurant.app.core.exception.BusinessException;
import fiap.restaurant.app.core.gateway.UserTypeGateway;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DeleteUserTypeUseCaseImpl implements DeleteUserTypeUseCase {

    private final UserTypeGateway userTypeGateway;
    private final FindUserTypeByIdUseCase findUserTypeByIdUseCase;

    private static final List<String> RESERVED_TYPES = Arrays.asList(
            UserType.CUSTOMER,
            UserType.OWNER
    );

    public DeleteUserTypeUseCaseImpl(UserTypeGateway userTypeGateway, FindUserTypeByIdUseCase findUserTypeByIdUseCase) {
        this.userTypeGateway = userTypeGateway;
        this.findUserTypeByIdUseCase = findUserTypeByIdUseCase;
    }

    @Override
    public void execute(UUID id) {
        UserType userType = findUserTypeByIdUseCase.execute(id);

        if (RESERVED_TYPES.contains(userType.getName())) {
            throw new BusinessException("Cannot delete system default user type: " + userType.getName());
        }

        try {
            userTypeGateway.delete(id);
        } catch (Exception e) {
            throw new BusinessException("Error deleting user type. It might be in use by users.");
        }
    }
} 