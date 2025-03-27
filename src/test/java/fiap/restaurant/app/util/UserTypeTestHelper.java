package fiap.restaurant.app.util;

import fiap.restaurant.app.adapter.web.json.usertype.UserTypeResponseDTO;
import fiap.restaurant.app.core.domain.UserType;

import java.util.UUID;

public class UserTypeTestHelper {

    public static final UUID CUSTOMER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    public static final UUID OWNER_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    public static UserType createUserTypeDomain(String typeName) {
        return UserType.builder()
                .id(typeName.equals(UserType.CUSTOMER) ? CUSTOMER_ID : OWNER_ID)
                .name(typeName)
                .build();
    }

    public static UserType createCustomerDomain() {
        return createUserTypeDomain(UserType.CUSTOMER);
    }

    public static UserType createOwnerDomain() {
        return createUserTypeDomain(UserType.OWNER);
    }

    public static UserTypeResponseDTO createUserTypeDTO(String typeName) {
        UserTypeResponseDTO dto = new UserTypeResponseDTO();
        dto.setId(typeName.equals(UserType.CUSTOMER) ? CUSTOMER_ID : OWNER_ID);
        dto.setName(typeName);
        return dto;
    }

    public static UserTypeResponseDTO createCustomerDTO() {
        return createUserTypeDTO(UserType.CUSTOMER);
    }

    public static UserTypeResponseDTO createOwnerDTO() {
        return createUserTypeDTO(UserType.OWNER);
    }
} 