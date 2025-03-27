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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserTypeUseCaseImplTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    @Mock
    private FindUserTypeByIdUseCaseImpl findUserTypeByIdUseCaseImpl;

    private UpdateUserTypeUseCaseImpl updateUserTypeUseCaseImpl;

    @BeforeEach
    void setUp() {
        updateUserTypeUseCaseImpl = new UpdateUserTypeUseCaseImpl(userTypeGateway, findUserTypeByIdUseCaseImpl);
    }

    @Test
    void execute_WithValidUpdate_ShouldReturnUpdatedUserType() {
        UUID id = UUID.randomUUID();
        
        UserType existingUserType = UserType.builder()
                .id(id)
                .name("OLD_TYPE")
                .build();
        
        UserType userTypeToUpdate = UserType.builder()
                .name("NEW_TYPE")
                .build();
        
        when(findUserTypeByIdUseCaseImpl.execute(id)).thenReturn(existingUserType);
        when(userTypeGateway.findByName(userTypeToUpdate.getName())).thenReturn(Optional.empty());
        
        UserType result = updateUserTypeUseCaseImpl.execute(id, userTypeToUpdate.getName());
        
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("NEW_TYPE", result.getName());
        
        verify(findUserTypeByIdUseCaseImpl).execute(id);
        verify(userTypeGateway).findByName(userTypeToUpdate.getName());
        verify(userTypeGateway).update(any(UserType.class));
    }
    
    @Test
    void execute_WithNameConflict_ShouldThrowBusinessException() {
        UUID id = UUID.randomUUID();
        UUID otherId = UUID.randomUUID();
        
        UserType existingUserType = UserType.builder()
                .id(id)
                .name("OLD_TYPE")
                .build();
        
        UserType conflictingUserType = UserType.builder()
                .id(otherId)
                .name("CONFLICT_TYPE")
                .build();
        
        UserType userTypeToUpdate = UserType.builder()
                .name("CONFLICT_TYPE")
                .build();
        
        when(findUserTypeByIdUseCaseImpl.execute(id)).thenReturn(existingUserType);
        when(userTypeGateway.findByName(userTypeToUpdate.getName())).thenReturn(Optional.of(conflictingUserType));
        
        BusinessException exception = assertThrows(BusinessException.class,
                () -> updateUserTypeUseCaseImpl.execute(id, userTypeToUpdate.getName()));
        
        assertEquals("User type with name CONFLICT_TYPE already exists", exception.getMessage());
        
        verify(findUserTypeByIdUseCaseImpl).execute(id);
        verify(userTypeGateway).findByName(userTypeToUpdate.getName());
        verify(userTypeGateway, never()).update(any(UserType.class));
    }
    
    @Test
    void execute_WithSameNameDifferentCase_ShouldNotThrowException() {
        UUID id = UUID.randomUUID();
        
        UserType existingUserType = UserType.builder()
                .id(id)
                .name("OLD_TYPE")
                .build();
        
        UserType sameIdUserType = UserType.builder()
                .id(id)
                .name("SAME_TYPE")
                .build();
        
        UserType userTypeToUpdate = UserType.builder()
                .name("SAME_TYPE")
                .build();
        
        when(findUserTypeByIdUseCaseImpl.execute(id)).thenReturn(existingUserType);
        when(userTypeGateway.findByName(userTypeToUpdate.getName())).thenReturn(Optional.of(sameIdUserType));
        
        UserType result = updateUserTypeUseCaseImpl.execute(id, userTypeToUpdate.getName());
        
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("SAME_TYPE", result.getName());
        
        verify(findUserTypeByIdUseCaseImpl).execute(id);
        verify(userTypeGateway).findByName(userTypeToUpdate.getName());
        verify(userTypeGateway).update(any(UserType.class));
    }
} 