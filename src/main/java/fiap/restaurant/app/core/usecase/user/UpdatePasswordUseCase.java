package fiap.restaurant.app.core.usecase.user;

public interface UpdatePasswordUseCase {
    void execute(String login, String currentPassword, String newPassword);
} 