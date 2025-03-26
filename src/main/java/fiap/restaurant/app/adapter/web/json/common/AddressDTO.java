package fiap.restaurant.app.adapter.web.json.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private UUID id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
} 