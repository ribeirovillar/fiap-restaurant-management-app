package fiap.restaurant.app.core.usecase.usertype;

import fiap.restaurant.app.core.domain.UserType;

import java.util.UUID;

public interface FindUserTypeByIdUseCase {
    UserType execute(UUID id);
}
