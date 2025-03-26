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
    public void findByCuisineType_ShouldReturnMatchingRestaurants_WhenCuisineTypeExists() throws Exception {
        UUID ownerId = createTestOwner();
        
        String italianRestaurant1Name = "Italian Restaurant 1";
        String italianRestaurant2Name = "Italian Restaurant 2";
        
        createRestaurantWithCuisineType(ownerId, italianRestaurant1Name, CuisineType.ITALIAN);
        createRestaurantWithCuisineType(ownerId, italianRestaurant2Name, CuisineType.ITALIAN);
        createRestaurantWithCuisineType(ownerId, "Mexican Restaurant", CuisineType.MEXICAN);
        
        MvcResult findResult = mockMvc.perform(get("/api/v1/restaurants/cuisine/{cuisineType}", "ITALIAN")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        
        List<RestaurantDTO> restaurants = objectMapper.readValue(
                findResult.getResponse().getContentAsString(),
                new TypeReference<>() {});
        
        assertNotNull(restaurants);
        assertTrue(restaurants.size() >= 2);
        
        boolean foundItalian1 = false;
        boolean foundItalian2 = false;
        
        for (RestaurantDTO restaurant : restaurants) {
            assertEquals(CuisineType.ITALIAN, restaurant.getCuisineType());
            
            if (restaurant.getName().equals(italianRestaurant1Name)) {
                foundItalian1 = true;
            } else if (restaurant.getName().equals(italianRestaurant2Name)) {
                foundItalian2 = true;
            }
        }
        
        assertTrue(foundItalian1, "First Italian restaurant not found");
        assertTrue(foundItalian2, "Second Italian restaurant not found");
    }
    
    @Test
    public void findByCuisineType_ShouldReturnEmptyList_WhenCuisineTypeDoesNotExist() throws Exception {
        UUID ownerId = createTestOwner();
        createRestaurantWithCuisineType(ownerId, "Test Italian Restaurant", CuisineType.ITALIAN);
        
        MvcResult findResult = mockMvc.perform(get("/api/v1/restaurants/cuisine/{cuisineType}", "JAPANESE")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        
        List<RestaurantDTO> restaurants = objectMapper.readValue(
                findResult.getResponse().getContentAsString(),
                new TypeReference<>() {});
        
        assertNotNull(restaurants);
        assertTrue(restaurants.isEmpty());
    }
    
    private void createRestaurantWithCuisineType(UUID ownerId, String name, CuisineType cuisineType) throws Exception {
        CreateRestaurantDTO createDTO = createRestaurantDTO(ownerId);
        createDTO.setName(name);
        createDTO.setCuisineType(cuisineType);
        
        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated());
    }
} 