package fiap.restaurant.app.adapter.presenter.impl;

import fiap.restaurant.app.adapter.database.jpa.entity.AddressEntity;
import fiap.restaurant.app.adapter.database.jpa.entity.UserEntity;
import fiap.restaurant.app.adapter.database.jpa.entity.UserTypeEntity;
import fiap.restaurant.app.adapter.presenter.AddressPresenter;
import fiap.restaurant.app.adapter.presenter.UserPresenter;
import fiap.restaurant.app.adapter.presenter.UserTypePresenter;
import fiap.restaurant.app.adapter.web.json.user.CreateUserDTO;
import fiap.restaurant.app.adapter.web.json.user.UpdateUserDTO;
import fiap.restaurant.app.adapter.web.json.user.UserResponseDTO;
import fiap.restaurant.app.adapter.web.json.usertype.UserTypeResponseDTO;
import fiap.restaurant.app.core.domain.Address;
import fiap.restaurant.app.core.domain.User;
import fiap.restaurant.app.core.domain.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class UserPresenterImpl implements UserPresenter {

    @Autowired
    private AddressPresenter addressPresenter;
    
    @Autowired
    private UserTypePresenter userTypePresenter;
    
    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public User mapToDomain(CreateUserDTO createUserRequestDTO) {
        if (createUserRequestDTO == null) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.address(addressPresenter.mapToDomain(createUserRequestDTO.getAddress()));
        user.name(createUserRequestDTO.getName());
        user.email(createUserRequestDTO.getEmail());
        user.password(createUserRequestDTO.getPassword());
        user.login(createUserRequestDTO.getLogin());
        
        if (createUserRequestDTO.getUserType() != null) {
            UserType userType = UserType.builder()
                    .id(createUserRequestDTO.getUserType().getId())
                    .name(createUserRequestDTO.getUserType().getName())
                    .build();
            user.userType(userType);
        }

        return user.build();
    }

    @Override
    public UserEntity mapToEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.address(addressToAddressEntity(user.getAddress()));
        userEntity.id(user.getId());
        userEntity.name(user.getName());
        userEntity.email(user.getEmail());
        userEntity.login(user.getLogin());
        userEntity.password(user.getPassword());
        
        if (user.getUserType() != null) {
            UserTypeEntity userTypeEntity = userTypePresenter.mapToEntity(user.getUserType());
            userEntity.userType(userTypeEntity);
        }

        userEntity.lastModifiedDate(LocalDateTime.now());

        return userEntity.build();
    }

    @Override
    public User mapToDomain(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.address(addressPresenter.mapToDomain(userEntity.getAddress()));
        if (userEntity.getLastModifiedDate() != null) {
            user.lastModifiedDate(dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format(userEntity.getLastModifiedDate()));
        }
        user.id(userEntity.getId());
        user.name(userEntity.getName());
        user.email(userEntity.getEmail());
        user.password(userEntity.getPassword());
        user.login(userEntity.getLogin());
        
        if (userEntity.getUserType() != null) {
            UserType userType = userTypePresenter.mapToDomain(userEntity.getUserType());
            user.userType(userType);
        }

        return user.build();
    }

    @Override
    public UserResponseDTO mapToResponseDTO(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setAddress(addressPresenter.mapToDTO(user.getAddress()));
        userResponseDTO.setId(user.getId());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setLogin(user.getLogin());
        
        if (user.getUserType() != null) {
            UserTypeResponseDTO userTypeResponseDTO = userTypePresenter.mapToResponseDTO(user.getUserType());
            userResponseDTO.setUserType(userTypeResponseDTO);
        }
        
        userResponseDTO.setLastModifiedDate(user.getLastModifiedDate());

        return userResponseDTO;
    }

    @Override
    public User mapToDomain(UpdateUserDTO updateUserDTO) {
        if (updateUserDTO == null) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.address(addressPresenter.mapToDomain(updateUserDTO.getAddress()));
        user.name(updateUserDTO.getName());
        user.login(updateUserDTO.getLogin());
        user.email(updateUserDTO.getEmail());
        
        if (updateUserDTO.getUserType() != null) {
            UserType userType = UserType.builder()
                    .id(updateUserDTO.getUserType().getId())
                    .name(updateUserDTO.getUserType().getName())
                    .build();
            user.userType(userType);
        }

        return user.build();
    }

    protected AddressEntity addressToAddressEntity(Address address) {
        if (address == null) {
            return null;
        }

        AddressEntity.AddressEntityBuilder addressEntity = AddressEntity.builder();

        addressEntity.id(address.getId());
        addressEntity.street(address.getStreet());
        addressEntity.city(address.getCity());
        addressEntity.state(address.getState());
        addressEntity.zipCode(address.getZipCode());
        addressEntity.country(address.getCountry());

        return addressEntity.build();
    }
}
