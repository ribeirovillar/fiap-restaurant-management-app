package fiap.restaurant.app.adapter.web.json.menuitem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpsertMenuItemDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private boolean availableForTakeout;
    private String photoPath;
} 