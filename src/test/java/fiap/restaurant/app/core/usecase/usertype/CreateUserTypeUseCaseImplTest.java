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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserTypeUseCaseImplTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    private CreateUserTypeUseCaseImpl createUserTypeUseCaseImpl;

    @BeforeEach
    void setUp() {
        createUserTypeUseCaseImpl = new CreateUserTypeUseCaseImpl(userTypeGateway);
    }

    @Test
    void execute_WithValidUserType_ShouldReturnCreatedUserType() {
        String typeName = "NEW_TYPE";
        
        UUID createdId = UUID.randomUUID();
        UserType createdUserType = UserType.builder()
                .id(createdId)
                .name(typeName)
                .build();

        when(userTypeGateway.existsByName(typeName)).thenReturn(false);
        doReturn(createdUserType).when(userTypeGateway).create(any(UserType.class));

        UserType result = createUserTypeUseCaseImpl.execute(typeName);

        assertNotNull(result);
        assertEquals(createdId, result.getId());
        assertEquals(typeName, result.getName());

        verify(userTypeGateway).existsByName(typeName);
        verify(userTypeGateway).create(any(UserType.class));
    }

    @Test
    void execute_WithExistingUserTypeName_ShouldThrowBusinessException() {
        UserType userType = UserType.builder()
                .name("EXISTING_TYPE")
                .build();

        when(userTypeGateway.existsByName(userType.getName())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> createUserTypeUseCaseImpl.execute(userType.getName()));

        assertEquals("User type with name EXISTING_TYPE already exists", exception.getMessage());

        verify(userTypeGateway).existsByName(userType.getName());
        verify(userTypeGateway, never()).create(any(UserType.class));
    }
} 