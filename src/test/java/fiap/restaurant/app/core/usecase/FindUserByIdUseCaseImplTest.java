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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserByIdUseCaseImplTest {

    @Mock
    private UserGateway userGateway;

    private FindUserByIdUseCaseImpl findUserByIdUseCase;

    @BeforeEach
    void setUp() {
        findUserByIdUseCase = new FindUserByIdUseCaseImpl(userGateway);
    }

    @Test
    void shouldFindUserById() {
        UUID userId = UUID.randomUUID();
        User expectedUser = User.builder()
                .id(userId)
                .name("Test User")
                .email("test@example.com")
                .login("testuser")
                .password("encodedPassword")
                .userType(UserType.CUSTOMER)
                .build();

        when(userGateway.findById(userId)).thenReturn(Optional.of(expectedUser));

        User result = findUserByIdUseCase.execute(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("testuser", result.getLogin());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(UserType.CUSTOMER, result.getUserType());

        verify(userGateway).findById(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userGateway.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> findUserByIdUseCase.execute(userId));

        assertEquals("User not found with id: " + userId, exception.getMessage());
        verify(userGateway).findById(userId);
    }
} 