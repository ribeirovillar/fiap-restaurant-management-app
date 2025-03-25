package fiap.restaurant.app.adapter.web.integration.restaurant;

import com.fasterxml.jackson.core.type.TypeReference;
import fiap.restaurant.app.adapter.web.json.restaurant.CreateRestaurantDTO;
import fiap.restaurant.app.adapter.web.json.restaurant.RestaurantDTO;
import fiap.restaurant.app.core.domain.CuisineType;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FindRestaurantsByCuisineTypeIntegrationTest extends BaseRestaurantIntegrationTest {

    @Test
    public void findRestaurantsByCuisineType_WithValidType_ReturnsRestaurants() throws Exception {
        // Create owner
        UUID ownerId = createTestOwner();
        
        // Create restaurant with ITALIAN cuisine type
        CreateRestaurantDTO createDTO1 = createRestaurantDTO(ownerId);
        createDTO1.setName("Italian Restaurant 1");
        createDTO1.setCuisineType(CuisineType.ITALIAN);
        
        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO1)))
                .andExpect(status().isCreated());
        
        // Create another restaurant with ITALIAN cuisine type
        CreateRestaurantDTO createDTO2 = createRestaurantDTO(ownerId);
        createDTO2.setName("Italian Restaurant 2");
        createDTO2.setCuisineType(CuisineType.ITALIAN);
        
        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO2)))
                .andExpect(status().isCreated());
        
        // Create restaurant with MEXICAN cuisine type
        CreateRestaurantDTO createDTO3 = createRestaurantDTO(ownerId);
        createDTO3.setName("Mexican Restaurant");
        createDTO3.setCuisineType(CuisineType.MEXICAN);
        
        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO3)))
                .andExpect(status().isCreated());
        
        // Find restaurants by cuisine type ITALIAN
        MvcResult findResult = mockMvc.perform(get("/api/v1/restaurants/cuisine/{cuisineType}", "ITALIAN")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        
        List<RestaurantDTO> restaurants = objectMapper.readValue(
                findResult.getResponse().getContentAsString(),
                new TypeReference<List<RestaurantDTO>>() {});
        
        assertNotNull(restaurants);
        assertTrue(restaurants.size() >= 2); // At least our 2 restaurants
        
        boolean foundItalian1 = false;
        boolean foundItalian2 = false;
        
        for (RestaurantDTO restaurant : restaurants) {
            assertEquals(CuisineType.ITALIAN, restaurant.getCuisineType());
            
            if (restaurant.getName().equals("Italian Restaurant 1")) {
                foundItalian1 = true;
            } else if (restaurant.getName().equals("Italian Restaurant 2")) {
                foundItalian2 = true;
            }
        }
        
        assertTrue(foundItalian1, "First Italian restaurant not found");
        assertTrue(foundItalian2, "Second Italian restaurant not found");
    }
    
    @Test
    public void findRestaurantsByCuisineType_WithNonExistingType_ReturnsEmptyList() throws Exception {
        // Create owner and restaurant with ITALIAN cuisine
        UUID ownerId = createTestOwner();
        CreateRestaurantDTO createDTO = createRestaurantDTO(ownerId);
        createDTO.setCuisineType(CuisineType.ITALIAN);
        
        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated());
        
        // Find restaurants by cuisine type JAPANESE (non-existing)
        MvcResult findResult = mockMvc.perform(get("/api/v1/restaurants/cuisine/{cuisineType}", "JAPANESE")
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