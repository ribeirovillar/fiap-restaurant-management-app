package fiap.restaurant.app.core.usecase.usertype;

import fiap.restaurant.app.core.domain.UserType;
import fiap.restaurant.app.core.exception.BusinessException;
import fiap.restaurant.app.core.gateway.UserTypeGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUserTypeByIdUseCaseImplTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    private FindUserTypeByIdUseCaseImpl findUserTypeByIdUseCaseImpl;

    @BeforeEach
    void setUp() {
        findUserTypeByIdUseCaseImpl = new FindUserTypeByIdUseCaseImpl(userTypeGateway);
    }

    @Test
    void execute_WithExistingId_ShouldReturnUserType() {
        UUID id = UUID.randomUUID();
        UserType userType = UserType.builder()
                .id(id)
                .name("TEST_TYPE")
                .build();

        when(userTypeGateway.findById(id)).thenReturn(Optional.of(userType));

        UserType result = findUserTypeByIdUseCaseImpl.execute(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("TEST_TYPE", result.getName());

        verify(userTypeGateway).findById(id);
    }

    @Test
    void execute_WithNonExistingId_ShouldThrowBusinessException() {
        UUID id = UUID.randomUUID();

        when(userTypeGateway.findById(id)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> findUserTypeByIdUseCaseImpl.execute(id));

        assertEquals("User type with ID " + id + " not found", exception.getMessage());

        verify(userTypeGateway).findById(id);
    }
} 