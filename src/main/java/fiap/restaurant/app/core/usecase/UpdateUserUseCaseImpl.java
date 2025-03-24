package fiap.restaurant.app.core.usecase;

import fiap.restaurant.app.core.domain.User;
import fiap.restaurant.app.core.exception.DuplicatedDataException;
import fiap.restaurant.app.core.exception.EmailFormatException;
import fiap.restaurant.app.core.exception.UpdateUserException;
import fiap.restaurant.app.core.gateway.UserGateway;

import java.util.Optional;
import java.util.UUID;

public class UpdateUserUseCaseImpl implements UpdateUserUseCase {

    private final UserGateway userGateway;
    private final FindUserByIdUseCase findUserByIdUseCase;

    public UpdateUserUseCaseImpl(UserGateway userGateway, FindUserByIdUseCase findUserByIdUseCase) {
        this.userGateway = userGateway;
        this.findUserByIdUseCase = findUserByIdUseCase;
    }

    @Override
    public User execute(UUID id, User userUpdate) {
        return Optional.of(findUserByIdUseCase.execute(id))
                .map(existingUser -> {
                    if (userUpdate.getEmail() != null && !userUpdate.isValidEmail()) {
                        throw new EmailFormatException("Invalid email format: " + userUpdate.getEmail());
                    }

                    if (userUpdate.getEmail() != null && !existingUser.getEmail().equals(userUpdate.getEmail()) &&
                            userGateway.existsByEmail(userUpdate.getEmail())) {
                        throw new DuplicatedDataException("Email already in use by another user: " + userUpdate.getEmail());
                    }
                    
                    if (userUpdate.getLogin() != null && !existingUser.getLogin().equals(userUpdate.getLogin()) &&
                            userGateway.existsByLogin(userUpdate.getLogin())) {
                        throw new DuplicatedDataException("Login already in use by another user: " + userUpdate.getLogin());
                    }

                    if (userUpdate.getName() != null) {
                        existingUser.setName(userUpdate.getName());
                    }
                    
                    if (userUpdate.getEmail() != null) {
                        existingUser.setEmail(userUpdate.getEmail());
                    }
                    
                    if (userUpdate.getLogin() != null) {
                        existingUser.setLogin(userUpdate.getLogin());
                    }
                    
                    if (userUpdate.getUserType() != null) {
                        existingUser.setUserType(userUpdate.getUserType());
                    }

                    if (userUpdate.getAddress() != null) {
                        existingUser.setAddress(userUpdate.getAddress());
                    }

                    userGateway.update(existingUser);
                    return existingUser;
                }).orElseThrow(() -> new UpdateUserException("Failed to update user with id: " + id));
    }
} 