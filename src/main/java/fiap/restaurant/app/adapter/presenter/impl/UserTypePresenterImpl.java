package fiap.restaurant.app.adapter.presenter.impl;

import fiap.restaurant.app.adapter.database.jpa.entity.UserTypeEntity;
import fiap.restaurant.app.adapter.presenter.UserTypePresenter;
import fiap.restaurant.app.adapter.web.json.usertype.UserTypeResponseDTO;
import fiap.restaurant.app.core.domain.UserType;
import org.springframework.stereotype.Component;

@Component
public class UserTypePresenterImpl implements UserTypePresenter {

    @Override
    public UserTypeEntity mapToEntity(UserType userType) {
        if (userType == null) {
            return null;
        }

        UserTypeEntity.UserTypeEntityBuilder userTypeEntity = UserTypeEntity.builder();
        userTypeEntity.id(userType.getId());
        userTypeEntity.name(userType.getName());

        return userTypeEntity.build();
    }

    @Override
    public UserType mapToDomain(UserTypeEntity userTypeEntity) {
        if (userTypeEntity == null) {
            return null;
        }

        UserType.UserTypeBuilder userType = UserType.builder();
        userType.id(userTypeEntity.getId());
        userType.name(userTypeEntity.getName());

        return userType.build();
    }

    @Override
    public UserTypeResponseDTO mapToResponseDTO(UserType userType) {
        if (userType == null) {
            return null;
        }

        UserTypeResponseDTO userTypeResponseDTO = new UserTypeResponseDTO();
        userTypeResponseDTO.setId(userType.getId());
        userTypeResponseDTO.setName(userType.getName());

        return userTypeResponseDTO;
    }
} 