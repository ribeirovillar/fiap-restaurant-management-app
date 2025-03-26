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

public class FindAllRestaurantsIntegrationTest extends BaseRestaurantIntegrationTest {

    @Test
    public void findAllRestaurants_ReturnsAllRestaurants() throws Exception {
        UUID ownerId1 = createTestOwner();
        CreateRestaurantDTO createDTO1 = createRestaurantDTO(ownerId1);
        createDTO1.setName("Test Restaurant 1");
        
        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO1)))
                .andExpect(status().isCreated());
        
        UUID ownerId2 = createTestOwner();
        CreateRestaurantDTO createDTO2 = createRestaurantDTO(ownerId2);
        createDTO2.setName("Test Restaurant 2");
        
        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO2)))
                .andExpect(status().isCreated());
        
        MvcResult findResult = mockMvc.perform(get("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        
        List<RestaurantDTO> restaurants = objectMapper.readValue(
                findResult.getResponse().getContentAsString(),
                new TypeReference<>() {});
        
        assertNotNull(restaurants);
        assertTrue(restaurants.size() >= 2);
        
        boolean foundRestaurant1 = false;
        boolean foundRestaurant2 = false;
        
        for (RestaurantDTO restaurant : restaurants) {
            if (restaurant.getName().equals("Test Restaurant 1")) {
                foundRestaurant1 = true;
            } else if (restaurant.getName().equals("Test Restaurant 2")) {
                foundRestaurant2 = true;
            }
        }
        
        assertTrue(foundRestaurant1, "First restaurant not found in the list");
        assertTrue(foundRestaurant2, "Second restaurant not found in the list");
    }
} 