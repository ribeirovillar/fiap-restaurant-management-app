package fiap.restaurant.app.adapter.web.integration.restaurant;

import fiap.restaurant.app.adapter.web.json.restaurant.CreateRestaurantDTO;
import fiap.restaurant.app.adapter.web.json.restaurant.RestaurantDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteRestaurantIntegrationTest extends BaseRestaurantIntegrationTest {

    @Test
    public void deleteRestaurant_WithValidId_ReturnsNoContent() throws Exception {
        // Create restaurant to delete
        UUID ownerId = createTestOwner();
        CreateRestaurantDTO createDTO = createRestaurantDTO(ownerId);
        
        MvcResult createResult = mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andReturn();
                
        RestaurantDTO createdRestaurant = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                RestaurantDTO.class);
        
        // Delete restaurant
        mockMvc.perform(delete("/api/v1/restaurants/{id}", createdRestaurant.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        
        // Verify restaurant is deleted
        mockMvc.perform(get("/api/v1/restaurants/{id}", createdRestaurant.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void deleteRestaurant_WithInvalidId_ReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/restaurants/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
} 