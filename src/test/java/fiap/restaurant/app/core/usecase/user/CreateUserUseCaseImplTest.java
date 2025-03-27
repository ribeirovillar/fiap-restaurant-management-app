package fiap.restaurant.app.core.usecase.user;

import fiap.restaurant.app.core.domain.User;
import fiap.restaurant.app.core.domain.UserType;
import fiap.restaurant.app.core.exception.DuplicatedDataException;
import fiap.restaurant.app.core.exception.EmailFormatException;
import fiap.restaurant.app.core.gateway.UserGateway;
import fiap.restaurant.app.util.UserTypeTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseImplTest {

    @Mock
    private UserGateway userGateway;

    @Mock
    private PasswordEncoder passwordEncoder;

    private CreateUserUseCaseImpl createUserUseCase;

    @BeforeEach
    void setUp() {
        createUserUseCase = new CreateUserUseCaseImpl(userGateway, passwordEncoder);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        UUID userId = UUID.randomUUID();
        String email = "test@example.com";
        String login = "testuser";
        String password = "password123";
        String encodedPassword = "encodedPassword";
        UserType customerType = UserTypeTestHelper.createCustomerDomain();

        User user = User.builder()
                .email(email)
                .login(login)
                .password(password)
                .userType(customerType)
                .name("Test User")
                .build();

        User savedUser = User.builder()
                .id(userId)
                .email(email)
                .login(login)
                .password(encodedPassword)
                .userType(customerType)
                .name("Test User")
                .build();

        when(userGateway.existsByLogin(login)).thenReturn(false);
        when(userGateway.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userGateway.create(any(User.class))).thenReturn(savedUser);

        User result = createUserUseCase.execute(user);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(email, result.getEmail());
        assertEquals(login, result.getLogin());
        assertEquals(encodedPassword, result.getPassword());
        assertEquals(customerType, result.getUserType());
        
        verify(userGateway).existsByLogin(login);
        verify(userGateway).existsByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(userGateway).create(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        UserType customerType = UserTypeTestHelper.createCustomerDomain();
        
        User user = User.builder()
                .email("invalid-email")
                .login("testuser")
                .password("password123")
                .userType(customerType)
                .name("Test User")
                .build();

        EmailFormatException exception = assertThrows(EmailFormatException.class,
                () -> createUserUseCase.execute(user));
        
        assertEquals("Invalid email format: invalid-email", exception.getMessage());
        
        verify(userGateway, never()).existsByLogin(anyString());
        verify(userGateway, never()).existsByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userGateway, never()).create(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenLoginAlreadyExists() {
        String email = "test@example.com";
        String login = "existinglogin";
        UserType customerType = UserTypeTestHelper.createCustomerDomain();
        
        User user = User.builder()
                .email(email)
                .login(login)
                .password("password123")
                .userType(customerType)
                .name("Test User")
                .build();

        when(userGateway.existsByLogin(login)).thenReturn(true);

        DuplicatedDataException exception = assertThrows(DuplicatedDataException.class,
                () -> createUserUseCase.execute(user));
        
        assertEquals("Login already exists: existinglogin", exception.getMessage());
        
        verify(userGateway).existsByLogin(login);
        verify(userGateway, never()).existsByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userGateway, never()).create(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        String email = "existing@example.com";
        String login = "testuser";
        UserType customerType = UserTypeTestHelper.createCustomerDomain();
        
        User user = User.builder()
                .email(email)
                .login(login)
                .password("password123")
                .userType(customerType)
                .name("Test User")
                .build();

        when(userGateway.existsByLogin(login)).thenReturn(false);
        when(userGateway.existsByEmail(email)).thenReturn(true);

        DuplicatedDataException exception = assertThrows(DuplicatedDataException.class,
                () -> createUserUseCase.execute(user));
        
        assertEquals("Email already exists: existing@example.com", exception.getMessage());
        
        verify(userGateway).existsByLogin(login);
        verify(userGateway).existsByEmail(email);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userGateway, never()).create(any(User.class));
    }
} 