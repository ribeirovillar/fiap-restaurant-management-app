package fiap.restaurant.app.core.usecase.usertype;

import fiap.restaurant.app.core.domain.UserType;
import fiap.restaurant.app.core.gateway.UserTypeGateway;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllUserTypesUseCaseImplTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    private FindAllUserTypesUseCaseImpl findAllUserTypesUseCaseImpl;

    @BeforeEach
    void setUp() {
        findAllUserTypesUseCaseImpl = new FindAllUserTypesUseCaseImpl(userTypeGateway);
    }

    @Test
    void execute_WithMultipleUserTypes_ShouldReturnAllUserTypes() {
        UserType userType1 = UserType.builder()
                .id(UUID.randomUUID())
                .name("TYPE1")
                .build();
        
        UserType userType2 = UserType.builder()
                .id(UUID.randomUUID())
                .name("TYPE2")
                .build();
        
        List<UserType> userTypes = Arrays.asList(userType1, userType2);
        
        when(userTypeGateway.findAll()).thenReturn(userTypes);
        
        List<UserType> result = findAllUserTypesUseCaseImpl.execute();
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(userType1.getId(), result.get(0).getId());
        assertEquals(userType1.getName(), result.get(0).getName());
        assertEquals(userType2.getId(), result.get(1).getId());
        assertEquals(userType2.getName(), result.get(1).getName());
        
        verify(userTypeGateway).findAll();
    }
    
    @Test
    void execute_WithNoUserTypes_ShouldReturnEmptyList() {
        when(userTypeGateway.findAll()).thenReturn(Collections.emptyList());
        
        List<UserType> result = findAllUserTypesUseCaseImpl.execute();
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(userTypeGateway).findAll();
    }
} 