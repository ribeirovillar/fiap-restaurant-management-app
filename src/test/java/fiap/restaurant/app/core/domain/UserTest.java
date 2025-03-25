package fiap.restaurant.app.core.domain;

import fiap.restaurant.app.core.exception.EmailFormatException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldValidateEmail() {
        User user = User.builder()
                .email("valid@example.com")
                .build();
        
        assertTrue(user.isValidEmail());
        
        User invalidUser = User.builder()
                .email("invalid-email")
                .build();
        
        assertFalse(invalidUser.isValidEmail());
    }
    
    @Test
    void shouldThrowExceptionWhenSettingInvalidEmail() {
        User user = new User();
        
        EmailFormatException exception = assertThrows(EmailFormatException.class,
                () -> user.setEmail("invalid-email"));
        
        assertEquals("Invalid email format: invalid-email", exception.getMessage());
    }
    
    @Test
    void shouldAcceptValidEmail() {
        User user = new User();
        
        assertDoesNotThrow(() -> user.setEmail("valid@example.com"));
        assertEquals("valid@example.com", user.getEmail());
    }
    
    @Test
    void shouldThrowExceptionWhenSettingShortName() {
        User user = new User();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> user.setName("Ab"));
        
        assertEquals("Name must be at least 3 characters long", exception.getMessage());
    }
    
    @Test
    void shouldAcceptValidName() {
        User user = new User();
        
        assertDoesNotThrow(() -> user.setName("Valid Name"));
        assertEquals("Valid Name", user.getName());
    }
    
    @Test
    void shouldThrowExceptionWhenSettingShortLogin() {
        User user = new User();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> user.setLogin("ab"));
        
        assertEquals("Login must be at least 3 characters long", exception.getMessage());
    }
    
    @Test
    void shouldAcceptValidLogin() {
        User user = new User();
        
        assertDoesNotThrow(() -> user.setLogin("validlogin"));
        assertEquals("validlogin", user.getLogin());
    }
    
    @Test
    void shouldThrowExceptionWhenSettingShortPassword() {
        User user = new User();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> user.setPassword("12345"));
        
        assertEquals("Password must be at least 6 characters long", exception.getMessage());
    }
    
    @Test
    void shouldAcceptValidPassword() {
        User user = new User();
        
        assertDoesNotThrow(() -> user.setPassword("validpassword"));
        assertEquals("validpassword", user.getPassword());
    }
    
    @Test
    void shouldThrowExceptionWhenUserTypeIsNull() {
        User user = new User();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> user.setUserType(null));
        
        assertEquals("User type cannot be null", exception.getMessage());
    }
    
    @Test
    void shouldAcceptValidUserType() {
        User user = new User();
        
        assertDoesNotThrow(() -> user.setUserType(UserType.CUSTOMER));
        assertEquals(UserType.CUSTOMER, user.getUserType());
    }
    
    @Test
    void shouldValidateAddressWhenSettingAddress() {
        User user = new User();
        Address address = Address.builder()
                .street("st")
                .build();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> user.setAddress(address));
        
        assertEquals("Street must be at least 3 characters long", exception.getMessage());
    }
    
    @Test
    void shouldAcceptValidAddress() {
        User user = new User();
        Address address = Address.builder()
                .street("Valid Street")
                .city("Valid City")
                .state("VS")
                .zipCode("12345")
                .country("Valid Country")
                .build();
        
        assertDoesNotThrow(() -> user.setAddress(address));
        assertEquals(address, user.getAddress());
    }
    
    @Test
    void shouldValidateForCreation() {
        User user = User.builder().build();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                user::validateForCreation);
        
        assertEquals("Name is required", exception.getMessage());
        
        User userWithInvalidEmail = User.builder()
                .name("Valid Name")
                .email("invalid-email")
                .login("validlogin")
                .password("validpassword")
                .userType(UserType.CUSTOMER)
                .build();
        
        EmailFormatException emailException = assertThrows(EmailFormatException.class,
                userWithInvalidEmail::validateForCreation);
        
        assertEquals("Invalid email format: invalid-email", emailException.getMessage());
        
        User validUser = User.builder()
                .name("Valid Name")
                .email("valid@example.com")
                .login("validlogin")
                .password("validpassword")
                .userType(UserType.CUSTOMER)
                .build();
        
        assertDoesNotThrow(validUser::validateForCreation);
    }
    
    @Test
    void shouldValidateForUpdate() {
        User existingUser = User.builder()
                .name("Existing Name")
                .email("existing@example.com")
                .login("existinglogin")
                .password("existingpassword")
                .userType(UserType.CUSTOMER)
                .build();
        
        User userWithInvalidEmail = User.builder()
                .email("invalid-email")
                .build();
        
        EmailFormatException emailException = assertThrows(EmailFormatException.class,
                () -> userWithInvalidEmail.validateForUpdate(existingUser));
        
        assertEquals("Invalid email format: invalid-email", emailException.getMessage());
        
        User validUpdate = User.builder()
                .name("Updated Name")
                .email("updated@example.com")
                .login("updatedlogin")
                .build();
        
        assertDoesNotThrow(() -> validUpdate.validateForUpdate(existingUser));
    }
} 