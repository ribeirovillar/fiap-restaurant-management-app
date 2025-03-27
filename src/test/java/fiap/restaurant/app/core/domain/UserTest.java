package fiap.restaurant.app.core.domain;

import fiap.restaurant.app.core.exception.EmailFormatException;
import fiap.restaurant.app.util.UserTypeTestUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void isValidEmail_ShouldReturnTrueForValidEmail() {
        User user = User.builder()
                .email("valid@example.com")
                .build();
        
        assertTrue(user.isValidEmail());
    }
    
    @Test
    void isValidEmail_ShouldReturnFalseForInvalidEmail() {
        User user = User.builder()
                .email("invalid-email")
                .build();
        
        assertFalse(user.isValidEmail());
    }
    
    @Test
    void setEmail_ShouldThrowException_WhenEmailIsInvalid() {
        User user = new User();
        
        EmailFormatException exception = assertThrows(EmailFormatException.class,
                () -> user.setEmail("invalidemail"));
        
        assertEquals("Invalid email format: invalidemail", exception.getMessage());
    }
    
    @Test
    void setEmail_ShouldSetEmail_WhenEmailIsValid() {
        User user = new User();
        String validEmail = "valid@email.com";
        
        assertDoesNotThrow(() -> user.setEmail(validEmail));
        assertEquals(validEmail, user.getEmail());
    }
    
    @Test
    void validateForCreation_ShouldNotThrowException_WhenUserIsValid() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setLogin("johndoe");
        user.setPassword("password123");
        user.setUserType(UserTypeTestUtil.createCustomerType());
        
        assertDoesNotThrow(user::validateForCreation);
    }
    
    @Test
    void validateForCreation_ShouldThrowException_WhenNameIsEmpty() {
        User user = new User();
        user.setEmail("john@example.com");
        user.setLogin("johndoe");
        user.setPassword("password123");
        user.setUserType(UserTypeTestUtil.createCustomerType());
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                user::validateForCreation);
        
        assertEquals("Name is required", exception.getMessage());
    }
    
    @Test
    void validateForCreation_ShouldThrowException_WhenEmailIsEmpty() {
        User user = new User();
        user.setName("John Doe");
        user.setLogin("johndoe");
        user.setPassword("password123");
        user.setUserType(UserTypeTestUtil.createCustomerType());
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                user::validateForCreation);
        
        assertEquals("Email is required", exception.getMessage());
    }
    
    @Test
    void setLogin_ShouldThrowException_WhenLoginIsTooShort() {
        User user = new User();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> user.setLogin("ab"));
        
        assertEquals("Login must be at least 3 characters long", exception.getMessage());
    }
    
    @Test
    void setLogin_ShouldSetLogin_WhenLoginIsValid() {
        User user = new User();
        String validLogin = "validlogin";
        
        assertDoesNotThrow(() -> user.setLogin(validLogin));
        assertEquals(validLogin, user.getLogin());
    }
    
    @Test
    void setPassword_ShouldThrowException_WhenPasswordIsTooShort() {
        User user = new User();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> user.setPassword("12345"));
        
        assertEquals("Password must be at least 6 characters long", exception.getMessage());
    }
    
    @Test
    void setPassword_ShouldSetPassword_WhenPasswordIsValid() {
        User user = new User();
        String validPassword = "validpassword";
        
        assertDoesNotThrow(() -> user.setPassword(validPassword));
        assertEquals(validPassword, user.getPassword());
    }
    
    @Test
    void setUserType_ShouldThrowException_WhenUserTypeIsNull() {
        User user = new User();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> user.setUserType(null));
        
        assertEquals("User type cannot be null", exception.getMessage());
    }
    
    @Test
    void setUserType_ShouldSetUserType_WhenUserTypeIsValid() {
        User user = new User();
        UserType validUserType = UserTypeTestUtil.createCustomerType();
        
        assertDoesNotThrow(() -> user.setUserType(validUserType));
        assertEquals(validUserType, user.getUserType());
    }
    
    @Test
    void setAddress_ShouldThrowException_WhenAddressIsInvalid() {
        User user = new User();
        Address invalidAddress = Address.builder()
                .street("st")
                .build();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> user.setAddress(invalidAddress));
        
        assertEquals("Street must be at least 3 characters long", exception.getMessage());
    }
    
    @Test
    void setAddress_ShouldSetAddress_WhenAddressIsValid() {
        User user = new User();
        Address validAddress = Address.builder()
                .street("Valid Street")
                .city("Valid City")
                .state("VS")
                .zipCode("12345")
                .country("Valid Country")
                .build();
        
        assertDoesNotThrow(() -> user.setAddress(validAddress));
        assertEquals(validAddress, user.getAddress());
    }
    
    @Test
    void validateForCreation_ShouldThrowException_WhenUserIsIncomplete() {
        User incompleteUser = User.builder().build();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                incompleteUser::validateForCreation);
        
        assertEquals("Name is required", exception.getMessage());
    }
    
    @Test
    void validateForCreation_ShouldThrowException_WhenEmailIsInvalid() {
        User userWithInvalidEmail = User.builder()
                .name("Valid Name")
                .email("invalid-email")
                .login("validlogin")
                .password("validpassword")
                .userType(UserTypeTestUtil.createCustomerType())
                .build();
        
        EmailFormatException emailException = assertThrows(EmailFormatException.class,
                userWithInvalidEmail::validateForCreation);
        
        assertEquals("Invalid email format: invalid-email", emailException.getMessage());
    }
    
    @Test
    void validateForCreation_ShouldNotThrowException_WhenUserIsComplete() {
        User validUser = User.builder()
                .name("Valid Name")
                .email("valid@example.com")
                .login("validlogin")
                .password("validpassword")
                .userType(UserTypeTestUtil.createOwnerType())
                .build();
        
        assertDoesNotThrow(validUser::validateForCreation);
    }
    
    @Test
    void validateForUpdate_ShouldThrowException_WhenEmailIsInvalid() {
        User existingUser = User.builder()
                .name("Existing Name")
                .email("existing@example.com")
                .login("existinglogin")
                .password("existingpassword")
                .userType(UserTypeTestUtil.createOwnerType())
                .build();
        
        User userWithInvalidEmail = User.builder()
                .email("invalid-email")
                .build();
        
        EmailFormatException emailException = assertThrows(EmailFormatException.class,
                () -> userWithInvalidEmail.validateForUpdate(existingUser));
        
        assertEquals("Invalid email format: invalid-email", emailException.getMessage());
    }
    
    @Test
    void validateForUpdate_ShouldNotThrowException_WhenUpdateIsValid() {
        User existingUser = User.builder()
                .name("Existing Name")
                .email("existing@example.com")
                .login("existinglogin")
                .password("existingpassword")
                .userType(UserTypeTestUtil.createOwnerType())
                .build();
        
        User validUpdate = User.builder()
                .name("Updated Name")
                .email("updated@example.com")
                .login("updatedlogin")
                .build();
        
        assertDoesNotThrow(() -> validUpdate.validateForUpdate(existingUser));
    }
} 