package fiap.restaurant.app.adapter.presenter;

import fiap.restaurant.app.adapter.database.jpa.entity.UserTypeEntity;
import fiap.restaurant.app.adapter.web.json.usertype.UserTypeResponseDTO;
import fiap.restaurant.app.core.domain.UserType;

public interface UserTypePresenter {

    UserTypeEntity mapToEntity(UserType userType);

    UserType mapToDomain(UserTypeEntity userTypeEntity);

    UserTypeResponseDTO mapToResponseDTO(UserType userType);
} 