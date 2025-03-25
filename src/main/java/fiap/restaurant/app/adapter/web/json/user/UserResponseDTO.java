package fiap.restaurant.app.adapter.web.json.user;

import fiap.restaurant.app.core.domain.UserType;
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
    private UserType userType;
    private String lastModifiedDate;
}
