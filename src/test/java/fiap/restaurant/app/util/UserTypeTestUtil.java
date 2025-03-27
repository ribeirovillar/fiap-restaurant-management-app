package fiap.restaurant.app.util;

import fiap.restaurant.app.core.domain.UserType;

import java.util.UUID;

public class UserTypeTestUtil {
    
    public static UserType createUserType(String userTypeName) {
        return UserType.builder()
                .id(UUID.randomUUID())
                .name(userTypeName)
                .build();
    }
    
    public static UserType createCustomerType() {
        return createUserType(UserType.CUSTOMER);
    }
    
    public static UserType createOwnerType() {
        return createUserType(UserType.OWNER);
    }
} 