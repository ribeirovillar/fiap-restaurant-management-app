package fiap.restaurant.app.core.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTypeTest {

    @Test
    void createUserType_WithValidData_ShouldReturnUserType() {
        UUID id = UUID.randomUUID();
        String name = "TEST_TYPE";
        
        UserType userType = UserType.builder()
                .id(id)
                .name(name)
                .build();
        
        assertEquals(id, userType.getId());
        assertEquals(name, userType.getName());
    }
    
    @Test
    void createUserType_WithNullId_ShouldReturnUserTypeWithNullId() {
        String name = "TEST_TYPE";
        
        UserType userType = UserType.builder()
                .name(name)
                .build();
        
        assertNull(userType.getId());
        assertEquals(name, userType.getName());
    }
    
    @Test
    void constants_ShouldHaveCorrectValues() {
        assertEquals("CUSTOMER", UserType.CUSTOMER);
        assertEquals("OWNER", UserType.OWNER);
    }
} 