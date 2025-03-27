package fiap.restaurant.app.core.usecase.usertype;

import fiap.restaurant.app.core.domain.UserType;

import java.util.List;

public interface FindAllUserTypesUseCase {
    List<UserType> execute();
}
