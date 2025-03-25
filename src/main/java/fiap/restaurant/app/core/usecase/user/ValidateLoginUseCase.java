package fiap.restaurant.app.core.usecase.user;

public interface ValidateLoginUseCase {
    void execute(String login, String password);
} 