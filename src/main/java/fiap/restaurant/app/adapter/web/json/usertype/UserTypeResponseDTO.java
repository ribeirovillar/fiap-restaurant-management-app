package fiap.restaurant.app.adapter.web.json.usertype;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTypeResponseDTO {
    private UUID id;
    private String name;
} 