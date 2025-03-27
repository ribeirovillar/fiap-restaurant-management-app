package fiap.restaurant.app.core.usecase.usertype;

import fiap.restaurant.app.core.domain.UserType;

import java.util.UUID;

public interface UpdateUserTypeUseCase {
    UserType execute(UUID id, String name);
}
