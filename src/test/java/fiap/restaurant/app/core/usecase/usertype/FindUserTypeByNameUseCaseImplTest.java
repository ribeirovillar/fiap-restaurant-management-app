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
class FindUserTypeByNameUseCaseImplTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    private FindUserTypeByNameUseCaseImpl findUserTypeByNameUseCaseImpl;

    @BeforeEach
    void setUp() {
        findUserTypeByNameUseCaseImpl = new FindUserTypeByNameUseCaseImpl(userTypeGateway);
    }

    @Test
    void execute_WithExistingName_ShouldReturnUserType() {
        String name = "TEST_TYPE";
        UserType userType = UserType.builder()
                .id(UUID.randomUUID())
                .name(name)
                .build();

        when(userTypeGateway.findByName(name)).thenReturn(Optional.of(userType));

        UserType result = findUserTypeByNameUseCaseImpl.execute(name);

        assertNotNull(result);
        assertEquals(name, result.getName());

        verify(userTypeGateway).findByName(name);
    }

    @Test
    void execute_WithNonExistingName_ShouldThrowBusinessException() {
        String name = "NON_EXISTING_TYPE";

        when(userTypeGateway.findByName(name)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> findUserTypeByNameUseCaseImpl.execute(name));

        assertEquals("User type with name " + name + " not found", exception.getMessage());

        verify(userTypeGateway).findByName(name);
    }
} 