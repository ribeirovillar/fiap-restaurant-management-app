package fiap.restaurant.app.core.usecase.usertype;

import fiap.restaurant.app.core.domain.UserType;

public interface CreateUserTypeUseCase {
    UserType execute(String name);
}
