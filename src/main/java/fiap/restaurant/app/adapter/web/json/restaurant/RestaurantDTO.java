package fiap.restaurant.app.adapter.web.json.restaurant;

import fiap.restaurant.app.adapter.web.json.common.AddressDTO;
import fiap.restaurant.app.core.domain.CuisineType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {
    private UUID id;
    private String name;
    private AddressDTO address;
    private CuisineType cuisineType;
    private List<BusinessHoursDTO> businessHours;
    private UUID ownerId;
    private String ownerName;
    private String createdAt;
    private String updatedAt;
} 