package fiap.restaurant.app.core.usecase;

import fiap.restaurant.app.core.domain.Address;
import fiap.restaurant.app.core.domain.User;
import fiap.restaurant.app.core.domain.UserType;
import fiap.restaurant.app.core.exception.DuplicatedDataException;
import fiap.restaurant.app.core.exception.EmailFormatException;
import fiap.restaurant.app.core.exception.UserNotFoundException;
import fiap.restaurant.app.core.gateway.UserGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseImplTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private FindUserByIdUseCase findUserByIdUseCase;

    private UpdateUserUseCaseImpl updateUserUseCase;

    @BeforeEach
    void setUp() {
        updateUserUseCase = new UpdateUserUseCaseImpl(userGateway, findUserByIdUseCase);
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        UUID userId = UUID.randomUUID();

        User existingUser = User.builder()
                .id(userId)
                .name("Old Name")
                .email("old@example.com")
                .login("oldlogin")
                .password("encodedPassword")
                .userType(UserType.CUSTOMER)
                .build();

        User userUpdate = User.builder()
                .name("New Name")
                .email("new@example.com")
                .login("newlogin")
                .userType(UserType.OWNER)
                .address(Address.builder()
                        .street("New Street")
                        .city("New City")
                        .state("NS")
                        .zipCode("12345")
                        .country("New Country")
                        .build())
                .build();

        when(findUserByIdUseCase.execute(userId)).thenReturn(existingUser);
        when(userGateway.existsByEmail("new@example.com")).thenReturn(false);
        when(userGateway.existsByLogin("newlogin")).thenReturn(false);
        doNothing().when(userGateway).update(any(User.class));


        User result = updateUserUseCase.execute(userId, userUpdate);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("New Name", result.getName());
        assertEquals("new@example.com", result.getEmail());
        assertEquals("newlogin", result.getLogin());
        assertEquals(UserType.OWNER, result.getUserType());
        assertNotNull(result.getAddress());
        assertEquals("New Street", result.getAddress().getStreet());

        verify(findUserByIdUseCase).execute(userId);
        verify(userGateway).existsByEmail("new@example.com");
        verify(userGateway).existsByLogin("newlogin");
        verify(userGateway).update(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenInvalidEmail() {
        UUID userId = UUID.randomUUID();

        User existingUser = User.builder()
                .id(userId)
                .name("Old Name")
                .email("old@example.com")
                .login("oldlogin")
                .password("encodedPassword")
                .userType(UserType.CUSTOMER)
                .build();

        User userUpdate = User.builder()
                .email("invalid-email")
                .build();

        when(findUserByIdUseCase.execute(userId)).thenReturn(existingUser);

        EmailFormatException exception = assertThrows(EmailFormatException.class,
                () -> updateUserUseCase.execute(userId, userUpdate));

        assertEquals("Invalid email format: invalid-email", exception.getMessage());

        verify(findUserByIdUseCase).execute(userId);
        verify(userGateway, never()).existsByEmail(anyString());
        verify(userGateway, never()).existsByLogin(anyString());
        verify(userGateway, never()).update(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        UUID userId = UUID.randomUUID();

        User existingUser = User.builder()
                .id(userId)
                .name("Old Name")
                .email("old@example.com")
                .login("oldlogin")
                .password("encodedPassword")
                .userType(UserType.CUSTOMER)
                .build();

        User userUpdate = User.builder()
                .email("existing@example.com")
                .build();

        when(findUserByIdUseCase.execute(userId)).thenReturn(existingUser);
        when(userGateway.existsByEmail("existing@example.com")).thenReturn(true);

        DuplicatedDataException exception = assertThrows(DuplicatedDataException.class,
                () -> updateUserUseCase.execute(userId, userUpdate));

        assertEquals("Email already in use by another user: existing@example.com", exception.getMessage());

        verify(findUserByIdUseCase).execute(userId);
        verify(userGateway).existsByEmail("existing@example.com");
        verify(userGateway, never()).existsByLogin(anyString());
        verify(userGateway, never()).update(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenLoginAlreadyExists() {
        UUID userId = UUID.randomUUID();

        User existingUser = User.builder()
                .id(userId)
                .name("Old Name")
                .email("old@example.com")
                .login("oldlogin")
                .password("encodedPassword")
                .userType(UserType.CUSTOMER)
                .build();

        User userUpdate = User.builder()
                .login("existinglogin")
                .build();

        when(findUserByIdUseCase.execute(userId)).thenReturn(existingUser);
        when(userGateway.existsByLogin("existinglogin")).thenReturn(true);

        DuplicatedDataException exception = assertThrows(DuplicatedDataException.class,
                () -> updateUserUseCase.execute(userId, userUpdate));

        assertEquals("Login already in use by another user: existinglogin", exception.getMessage());

        verify(findUserByIdUseCase).execute(userId);
        verify(userGateway, never()).existsByEmail(anyString());
        verify(userGateway).existsByLogin("existinglogin");
        verify(userGateway, never()).update(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();

        User userUpdate = User.builder()
                .name("New Name")
                .build();

        when(findUserByIdUseCase.execute(userId)).thenThrow(new UserNotFoundException("User not found with id: " + userId));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> updateUserUseCase.execute(userId, userUpdate));

        assertEquals("User not found with id: " + userId, exception.getMessage());

        verify(findUserByIdUseCase).execute(userId);
        verify(userGateway, never()).existsByEmail(anyString());
        verify(userGateway, never()).existsByLogin(anyString());
        verify(userGateway, never()).update(any(User.class));
    }
} 