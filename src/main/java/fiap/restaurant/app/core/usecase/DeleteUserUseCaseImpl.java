package fiap.restaurant.app.core.usecase;

import fiap.restaurant.app.core.gateway.UserGateway;

import java.util.UUID;

public class DeleteUserUseCaseImpl implements DeleteUserUseCase {

    private final UserGateway userGateway;
    private final FindUserByIdUseCase findUserByIdUseCase;

    public DeleteUserUseCaseImpl(UserGateway userGateway, FindUserByIdUseCase findUserByIdUseCase) {
        this.userGateway = userGateway;
        this.findUserByIdUseCase = findUserByIdUseCase;
    }

    @Override
    public void execute(UUID id) {
        findUserByIdUseCase.execute(id);
        userGateway.delete(id);
    }
} 