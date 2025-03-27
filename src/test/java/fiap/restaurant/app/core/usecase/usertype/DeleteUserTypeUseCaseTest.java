package fiap.restaurant.app.core.usecase.usertype;

import fiap.restaurant.app.core.domain.UserType;
import fiap.restaurant.app.core.exception.BusinessException;
import fiap.restaurant.app.core.gateway.UserTypeGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserTypeUseCaseTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    @Mock
    private FindUserTypeByIdUseCase findUserTypeByIdUseCase;

    private DeleteUserTypeUseCase deleteUserTypeUseCase;

    @BeforeEach
    void setUp() {
        deleteUserTypeUseCase = new DeleteUserTypeUseCaseImpl(userTypeGateway, findUserTypeByIdUseCase);
    }

    @Test
    void execute_WithValidId_ShouldDeleteUserType() {
        UUID id = UUID.randomUUID();
        UserType userType = UserType.builder()
                .id(id)
                .name("TEST_TYPE")
                .build();

        when(findUserTypeByIdUseCase.execute(id)).thenReturn(userType);
        doNothing().when(userTypeGateway).delete(id);

        assertDoesNotThrow(() -> deleteUserTypeUseCase.execute(id));

        verify(findUserTypeByIdUseCase).execute(id);
        verify(userTypeGateway).delete(id);
    }

    @Test
    void execute_WithReservedUserType_ShouldThrowBusinessException() {
        UUID id = UUID.randomUUID();
        UserType userType = UserType.builder()
                .id(id)
                .name(UserType.CUSTOMER)
                .build();

        when(findUserTypeByIdUseCase.execute(id)).thenReturn(userType);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> deleteUserTypeUseCase.execute(id));

        assertEquals("Cannot delete system default user type: " + UserType.CUSTOMER, exception.getMessage());

        verify(findUserTypeByIdUseCase).execute(id);
        verify(userTypeGateway, never()).delete(any(UUID.class));
    }

    @Test
    void execute_WithExecuteError_ShouldThrowBusinessException() {
        UUID id = UUID.randomUUID();
        UserType userType = UserType.builder()
                .id(id)
                .name("TEST_TYPE")
                .build();

        when(findUserTypeByIdUseCase.execute(id)).thenReturn(userType);
        doThrow(new RuntimeException("Database error")).when(userTypeGateway).delete(id);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> deleteUserTypeUseCase.execute(id));

        assertEquals("Error deleting user type. It might be in use by users.", exception.getMessage());

        verify(findUserTypeByIdUseCase).execute(id);
        verify(userTypeGateway).delete(id);
    }
} 