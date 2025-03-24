package fiap.restaurant.app.adapter.presenter;

import fiap.restaurant.app.adapter.database.jpa.entity.UserEntity;
import fiap.restaurant.app.adapter.web.json.CreateUserDTO;
import fiap.restaurant.app.adapter.web.json.UpdateUserDTO;
import fiap.restaurant.app.adapter.web.json.UserResponseDTO;
import fiap.restaurant.app.core.domain.User;
import org.mapstruct.Mapping;

public interface UserPresenter {

    @Mapping(source = "address", target = "address")
    User mapToDomain(CreateUserDTO createUserRequestDTO);
    
    @Mapping(source = "address", target = "address")
    User mapToDomain(UpdateUserDTO updateUserDTO);

    @Mapping(source = "address", target = "address")
    @Mapping(target = "address.user", ignore = true)
    @Mapping(target = "lastModifiedDate", expression = "java(LocalDateTime.now())")
    UserEntity mapToEntity(User user);

    @Mapping(source = "address", target = "address")
    @Mapping(source = "lastModifiedDate", target = "lastModifiedDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    User mapToDomain(UserEntity userEntity);

    @Mapping(source = "address", target = "address")
    UserResponseDTO mapToResponseDTO(User user);
}
