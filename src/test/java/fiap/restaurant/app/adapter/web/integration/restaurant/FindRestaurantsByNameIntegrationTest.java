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

public class FindRestaurantsByNameIntegrationTest extends BaseRestaurantIntegrationTest {

    @Test
    public void findRestaurantsByName_WithValidName_ReturnsRestaurants() throws Exception {
        // Create owner
        UUID ownerId = createTestOwner();
        
        // Create restaurant with matching name
        CreateRestaurantDTO createDTO1 = createRestaurantDTO(ownerId);
        createDTO1.setName("Special Pizza Place");
        
        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO1)))
                .andExpect(status().isCreated());
        
        // Create another restaurant with matching name
        CreateRestaurantDTO createDTO2 = createRestaurantDTO(ownerId);
        createDTO2.setName("Special Burger Joint");
        
        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO2)))
                .andExpect(status().isCreated());
        
        // Create another restaurant without matching name
        CreateRestaurantDTO createDTO3 = createRestaurantDTO(ownerId);
        createDTO3.setName("Regular Restaurant");
        
        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO3)))
                .andExpect(status().isCreated());
        
        // Find restaurants by name
        MvcResult findResult = mockMvc.perform(get("/api/v1/restaurants/name/{name}", "Special")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        
        List<RestaurantDTO> restaurants = objectMapper.readValue(
                findResult.getResponse().getContentAsString(),
                new TypeReference<List<RestaurantDTO>>() {});
        
        assertNotNull(restaurants);
        assertEquals(2, restaurants.size());
        
        for (RestaurantDTO restaurant : restaurants) {
            assertTrue(restaurant.getName().contains("Special"));
        }
    }
    
    @Test
    public void findRestaurantsByName_WithNonMatchingName_ReturnsEmptyList() throws Exception {
        // Create owner and restaurant
        UUID ownerId = createTestOwner();
        CreateRestaurantDTO createDTO = createRestaurantDTO(ownerId);
        
        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated());
        
        // Find restaurants by non-matching name
        MvcResult findResult = mockMvc.perform(get("/api/v1/restaurants/name/{name}", "NonExistentName")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        
        List<RestaurantDTO> restaurants = objectMapper.readValue(
                findResult.getResponse().getContentAsString(),
                new TypeReference<List<RestaurantDTO>>() {});
        
        assertNotNull(restaurants);
        assertTrue(restaurants.isEmpty());
    }
} 