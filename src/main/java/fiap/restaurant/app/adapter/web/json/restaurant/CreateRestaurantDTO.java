package fiap.restaurant.app.adapter.web.json.restaurant;

import fiap.restaurant.app.adapter.web.json.common.AddressDTO;
import fiap.restaurant.app.core.domain.CuisineType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
public class CreateRestaurantDTO {
    private String name;
    
    @Valid
    private AddressDTO address;
    
    private CuisineType cuisineType;
    
    @Valid
    private List<BusinessHoursDTO> businessHours;
    
    @NotNull(message = "Owner ID is required")
    private UUID ownerId;
} 