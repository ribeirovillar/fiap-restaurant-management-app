package fiap.restaurant.app.core.usecase.usertype;

import fiap.restaurant.app.core.domain.UserType;
import fiap.restaurant.app.core.exception.BusinessException;
import fiap.restaurant.app.core.gateway.UserTypeGateway;

import java.util.UUID;

public class UpdateUserTypeUseCaseImpl implements UpdateUserTypeUseCase {

    private final UserTypeGateway userTypeGateway;
    private final FindUserTypeByIdUseCase findUserTypeByIdUseCase;

    public UpdateUserTypeUseCaseImpl(UserTypeGateway userTypeGateway, FindUserTypeByIdUseCase findUserTypeByIdUseCase) {
        this.userTypeGateway = userTypeGateway;
        this.findUserTypeByIdUseCase = findUserTypeByIdUseCase;
    }

    @Override
    public UserType execute(UUID id, String name) {
        UserType existingUserType = findUserTypeByIdUseCase.execute(id);

        userTypeGateway.findByName(name)
                .ifPresent(userType -> {
                    if (!userType.getId().equals(id)) {
                        throw new BusinessException("User type with name " + name + " already exists");
                    }
                });

        UserType updatedUserType = UserType.builder()
                .id(existingUserType.getId())
                .name(name)
                .build();

        userTypeGateway.update(updatedUserType);

        return updatedUserType;
    }
}