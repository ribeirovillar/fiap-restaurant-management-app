package fiap.restaurant.app.adapter.web.integration.restaurant;

import com.fasterxml.jackson.core.type.TypeReference;
import fiap.restaurant.app.adapter.web.json.restaurant.CreateRestaurantDTO;
import fiap.restaurant.app.adapter.web.json.restaurant.RestaurantDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FindRestaurantsByOwnerIdIntegrationTest extends BaseRestaurantIntegrationTest {

    @Test
    public void findRestaurantsByOwnerId_WithValidOwnerId_ReturnsOwnerRestaurants() throws Exception {
        UUID ownerId = createTestOwner();
        
        CreateRestaurantDTO createDTO1 = createRestaurantDTO(ownerId);
        createDTO1.setName("Owner Restaurant 1");
        
        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO1)))
                .andExpect(status().isCreated());
        
        CreateRestaurantDTO createDTO2 = createRestaurantDTO(ownerId);
        createDTO2.setName("Owner Restaurant 2");
        
        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO2)))
                .andExpect(status().isCreated());
        
        UUID differentOwnerId = createTestOwner();
        CreateRestaurantDTO differentOwnerDTO = createRestaurantDTO(differentOwnerId);
        differentOwnerDTO.setName("Different Owner Restaurant");
        
        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(differentOwnerDTO)))
                .andExpect(status().isCreated());
        
        MvcResult findResult = mockMvc.perform(get("/api/v1/restaurants/owner/{ownerId}", ownerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        
        List<RestaurantDTO> restaurants = objectMapper.readValue(
                findResult.getResponse().getContentAsString(),
                new TypeReference<>() {});
        
        assertNotNull(restaurants);
        assertEquals(2, restaurants.size());
        
        for (RestaurantDTO restaurant : restaurants) {
            assertEquals(ownerId, restaurant.getOwnerId());
            assertTrue(
                restaurant.getName().equals("Owner Restaurant 1") || 
                restaurant.getName().equals("Owner Restaurant 2")
            );
        }
    }
    
    @Test
    public void findRestaurantsByOwnerId_WithInvalidOwnerId_ReturnsEmptyList() throws Exception {
        MvcResult findResult = mockMvc.perform(get("/api/v1/restaurants/owner/{ownerId}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        
        List<RestaurantDTO> restaurants = objectMapper.readValue(
                findResult.getResponse().getContentAsString(),
                new TypeReference<>() {});
        
        assertNotNull(restaurants);
        assertTrue(restaurants.isEmpty());
    }
} 