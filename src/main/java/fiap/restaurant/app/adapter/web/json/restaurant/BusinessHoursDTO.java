package fiap.restaurant.app.adapter.web.json.restaurant;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessHoursDTO {
    private DayOfWeek dayOfWeek;
    
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openingTime;
    
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closingTime;
    
    private boolean isClosed;
} 