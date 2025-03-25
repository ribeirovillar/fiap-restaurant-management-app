package fiap.restaurant.app.core.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    void shouldThrowExceptionWhenSettingShortStreet() {
        Address address = new Address();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> address.setStreet("st"));
        
        assertEquals("Street must be at least 3 characters long", exception.getMessage());
    }
    
    @Test
    void shouldAcceptValidStreet() {
        Address address = new Address();
        
        assertDoesNotThrow(() -> address.setStreet("Valid Street"));
        assertEquals("Valid Street", address.getStreet());
    }
    
    @Test
    void shouldThrowExceptionWhenSettingShortCity() {
        Address address = new Address();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> address.setCity("C"));
        
        assertEquals("City must be at least 2 characters long", exception.getMessage());
    }
    
    @Test
    void shouldAcceptValidCity() {
        Address address = new Address();
        
        assertDoesNotThrow(() -> address.setCity("Valid City"));
        assertEquals("Valid City", address.getCity());
    }
    
    @Test
    void shouldThrowExceptionWhenSettingShortState() {
        Address address = new Address();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> address.setState("S"));
        
        assertEquals("State must be at least 2 characters long", exception.getMessage());
    }
    
    @Test
    void shouldAcceptValidState() {
        Address address = new Address();
        
        assertDoesNotThrow(() -> address.setState("VS"));
        assertEquals("VS", address.getState());
    }
    
    @Test
    void shouldThrowExceptionWhenSettingShortZipCode() {
        Address address = new Address();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> address.setZipCode("1234"));
        
        assertEquals("Zip code must be at least 5 characters long", exception.getMessage());
    }
    
    @Test
    void shouldAcceptValidZipCode() {
        Address address = new Address();
        
        assertDoesNotThrow(() -> address.setZipCode("12345"));
        assertEquals("12345", address.getZipCode());
    }
    
    @Test
    void shouldSetCountryWithoutValidation() {
        Address address = new Address();
        
        assertDoesNotThrow(() -> address.setCountry("C"));
        assertEquals("C", address.getCountry());
    }
    
    @Test
    void shouldValidateForCreation() {
        Address address = Address.builder().build();
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                address::validateForCreation);
        
        assertEquals("Street is required", exception.getMessage());
        
        Address addressWithShortStreet = Address.builder()
                .street("st")
                .city("Valid City")
                .state("VS")
                .zipCode("12345")
                .country("Valid Country")
                .build();
        
        IllegalArgumentException streetException = assertThrows(IllegalArgumentException.class,
                addressWithShortStreet::validateForCreation);
        
        assertEquals("Street must be at least 3 characters long", streetException.getMessage());
        
        Address validAddress = Address.builder()
                .street("Valid Street")
                .city("Valid City")
                .state("VS")
                .zipCode("12345")
                .country("Valid Country")
                .build();
        
        assertDoesNotThrow(validAddress::validateForCreation);
    }
    
    @Test
    void shouldValidateForUpdate() {
        Address addressWithShortStreet = Address.builder()
                .street("st")
                .build();
        
        IllegalArgumentException streetException = assertThrows(IllegalArgumentException.class,
                addressWithShortStreet::validateForUpdate);
        
        assertEquals("Street must be at least 3 characters long", streetException.getMessage());
        
        Address validUpdate = Address.builder()
                .street("New Street")
                .city("New City")
                .build();
        
        assertDoesNotThrow(validUpdate::validateForUpdate);
    }
} 