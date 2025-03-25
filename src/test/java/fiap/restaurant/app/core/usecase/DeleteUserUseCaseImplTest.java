package fiap.restaurant.app.core.usecase;

import fiap.restaurant.app.core.domain.User;
import fiap.restaurant.app.core.domain.UserType;
import fiap.restaurant.app.core.exception.UserNotFoundException;
import fiap.restaurant.app.core.gateway.UserGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseImplTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private FindUserByIdUseCase findUserByIdUseCase;

    private DeleteUserUseCaseImpl deleteUserUseCase;

    @BeforeEach
    void setUp() {
        deleteUserUseCase = new DeleteUserUseCaseImpl(userGateway, findUserByIdUseCase);
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .name("Test User")
                .email("test@example.com")
                .login("testuser")
                .password("encodedPassword")
                .userType(UserType.CUSTOMER)
                .build();

        doNothing().when(userGateway).delete(userId);
        when(findUserByIdUseCase.execute(userId)).thenReturn(user);

        deleteUserUseCase.execute(userId);

        verify(findUserByIdUseCase).execute(userId);
        verify(userGateway).delete(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(findUserByIdUseCase.execute(userId)).thenThrow(new UserNotFoundException("User not found with id: " + userId));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> deleteUserUseCase.execute(userId));
        
        assertEquals("User not found with id: " + userId, exception.getMessage());
        verify(findUserByIdUseCase).execute(userId);
        verify(userGateway, never()).delete(any(UUID.class));
    }
} 