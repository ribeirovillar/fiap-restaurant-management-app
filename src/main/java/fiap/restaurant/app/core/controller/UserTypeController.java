package fiap.restaurant.app.core.controller;

import fiap.restaurant.app.core.domain.UserType;
import fiap.restaurant.app.core.usecase.usertype.*;

import java.util.List;
import java.util.UUID;

public class UserTypeController {

    private final CreateUserTypeUseCase createUserTypeUseCase;
    private final FindAllUserTypesUseCase findAllUserTypesUseCase;
    private final FindUserTypeByIdUseCase findUserTypeByIdUseCase;
    private final FindUserTypeByNameUseCase findUserTypeByNameUseCase;
    private final UpdateUserTypeUseCase updateUserTypeUseCase;
    private final DeleteUserTypeUseCase deleteUserTypeUseCase;

    public UserTypeController(CreateUserTypeUseCase createUserTypeUseCase, FindAllUserTypesUseCase findAllUserTypesUseCase, FindUserTypeByIdUseCase findUserTypeByIdUseCase, FindUserTypeByNameUseCase findUserTypeByNameUseCase, UpdateUserTypeUseCase updateUserTypeUseCase, DeleteUserTypeUseCase deleteUserTypeUseCase) {
        this.createUserTypeUseCase = createUserTypeUseCase;
        this.findAllUserTypesUseCase = findAllUserTypesUseCase;
        this.findUserTypeByIdUseCase = findUserTypeByIdUseCase;
        this.findUserTypeByNameUseCase = findUserTypeByNameUseCase;
        this.updateUserTypeUseCase = updateUserTypeUseCase;
        this.deleteUserTypeUseCase = deleteUserTypeUseCase;
    }

    public UserType create(String name) {
        return createUserTypeUseCase.execute(name);
    }

    public List<UserType> findAll() {
        return findAllUserTypesUseCase.execute();
    }

    public UserType findById(UUID id) {
        return findUserTypeByIdUseCase.execute(id);
    }

    public UserType findByName(String name) {
        return findUserTypeByNameUseCase.execute(name);
    }

    public UserType update(UUID id, String name) {
        return updateUserTypeUseCase.execute(id, name);
    }

    public void delete(UUID id) {
        deleteUserTypeUseCase.execute(id);
    }
} 