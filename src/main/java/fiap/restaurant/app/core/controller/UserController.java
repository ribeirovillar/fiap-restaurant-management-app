package fiap.restaurant.app.core.controller;

import fiap.restaurant.app.core.domain.User;
import fiap.restaurant.app.core.usecase.user.*;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class UserController {

    private CreateUserUseCase createUserUseCase;
    private FindAllUsersUseCase findAllUsersUseCase;
    private FindUserByIdUseCase findUserByIdUseCase;
    private UpdateUserUseCase updateUserUseCase;
    private DeleteUserUseCase deleteUserUseCase;
    private ValidateLoginUseCase validateLoginUseCase;
    private UpdatePasswordUseCase updatePasswordUseCase;

    public User create(User user) {
        return createUserUseCase.execute(user);
    }

    public List<User> findAll() {
        return findAllUsersUseCase.execute();
    }

    public User findById(UUID id) {
        return findUserByIdUseCase.execute(id);
    }

    public User update(UUID id, User userUpdate) {
        return updateUserUseCase.execute(id, userUpdate);
    }

    public void delete(UUID id) {
        deleteUserUseCase.execute(id);
    }

    public void validateLogin(String login, String password) {
        validateLoginUseCase.execute(login, password);
    }
    
    public void updatePassword(String login, String currentPassword, String newPassword) {
        updatePasswordUseCase.execute(login, currentPassword, newPassword);
    }
}
