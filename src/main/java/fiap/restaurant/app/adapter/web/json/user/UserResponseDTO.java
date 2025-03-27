package fiap.restaurant.app.adapter.web.json.user;

import fiap.restaurant.app.adapter.web.json.common.AddressDTO;
import fiap.restaurant.app.adapter.web.json.usertype.UserTypeResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private String login;
    private AddressDTO address;
    private UserTypeResponseDTO userType;
    private String lastModifiedDate;
}
