package fiap.restaurant.app.core.usecase;

public interface UpdatePasswordUseCase {
    void execute(String login, String currentPassword, String newPassword);
} 