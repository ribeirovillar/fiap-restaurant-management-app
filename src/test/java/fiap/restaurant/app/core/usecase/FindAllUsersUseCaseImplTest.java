package fiap.restaurant.app.core.usecase;

import fiap.restaurant.app.core.domain.User;
import fiap.restaurant.app.core.domain.UserType;
import fiap.restaurant.app.core.gateway.UserGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllUsersUseCaseImplTest {

    @Mock
    private UserGateway userGateway;

    private FindAllUsersUseCaseImpl findAllUsersUseCase;

    @BeforeEach
    void setUp() {
        findAllUsersUseCase = new FindAllUsersUseCaseImpl(userGateway);
    }

    @Test
    void shouldReturnAllUsers() {
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        User user1 = User.builder()
                .id(userId1)
                .name("User 1")
                .email("user1@example.com")
                .login("user1")
                .password("encodedPassword1")
                .userType(UserType.CUSTOMER)
                .build();

        User user2 = User.builder()
                .id(userId2)
                .name("User 2")
                .email("user2@example.com")
                .login("user2")
                .password("encodedPassword2")
                .userType(UserType.OWNER)
                .build();

        List<User> expectedUsers = Arrays.asList(user1, user2);
        when(userGateway.findAll()).thenReturn(expectedUsers);

        List<User> result = findAllUsersUseCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(userId1, result.get(0).getId());
        assertEquals("User 1", result.get(0).getName());
        assertEquals(userId2, result.get(1).getId());
        assertEquals("User 2", result.get(1).getName());

        verify(userGateway).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoUsersExist() {
        when(userGateway.findAll()).thenReturn(Collections.emptyList());

        List<User> result = findAllUsersUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userGateway).findAll();
    }
} 