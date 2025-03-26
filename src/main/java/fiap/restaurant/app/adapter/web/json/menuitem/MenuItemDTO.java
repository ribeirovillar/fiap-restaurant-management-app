package fiap.restaurant.app.adapter.web.json.menuitem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDTO {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean availableForTakeout;
    private String photoPath;
    private UUID restaurantId;
    private String restaurantName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 