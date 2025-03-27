package fiap.restaurant.app.adapter.web.integration.restaurant;

import fiap.restaurant.app.adapter.web.json.restaurant.CreateRestaurantDTO;
import fiap.restaurant.app.adapter.web.json.restaurant.RestaurantDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateRestaurantIntegrationTest extends BaseRestaurantIntegrationTest {

    @Test
    public void createRestaurant_WithValidData_ReturnsCreatedRestaurant() throws Exception {
        UUID ownerId = createTestOwner();
        CreateRestaurantDTO restaurantDTO = createRestaurantDTO(ownerId);

        MvcResult result = mockMvc.perform(post("/api/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(restaurantDTO.getName()))
                .andExpect(jsonPath("$.cuisineType").value(restaurantDTO.getCuisineType().toString()))
                .andExpect(jsonPath("$.ownerId").value(ownerId.toString()))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.businessHours").isArray())
                .andExpect(jsonPath("$.businessHours[0].dayOfWeek").value("MONDAY"))
                .andExpect(jsonPath("$.businessHours[0].openingTime").value("09:00"))
                .andExpect(jsonPath("$.businessHours[0].closingTime").value("18:00"))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        RestaurantDTO responseDTO = objectMapper.readValue(responseBody, RestaurantDTO.class);

        assertNotNull(responseDTO.getId());
        assertNotNull(responseDTO.getCreatedAt());
        assertNotNull(responseDTO.getUpdatedAt());
        assertEquals(restaurantDTO.getName(), responseDTO.getName());
        assertEquals(restaurantDTO.getCuisineType(), responseDTO.getCuisineType());
        assertEquals(ownerId, responseDTO.getOwnerId());
        assertNotNull(responseDTO.getBusinessHours());
        assertFalse(responseDTO.getBusinessHours().isEmpty());
        assertEquals(3, responseDTO.getBusinessHours().size());
    }

    @Test
    public void createRestaurant_WithInvalidOwner_ReturnsBadRequest() throws Exception {
        CreateRestaurantDTO restaurantDTO = createRestaurantDTO(UUID.randomUUID());

        mockMvc.perform(post("/api/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("User not found")));
    }

    @Test
    public void createRestaurant_WithShortName_ReturnsBadRequest() throws Exception {
        UUID ownerId = createTestOwner();
        CreateRestaurantDTO restaurantDTO = createRestaurantDTO(ownerId);
        restaurantDTO.setName("AB");

        mockMvc.perform(post("/api/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Restaurant name must be at least 3 characters long"));
    }

    @Test
    public void createRestaurant_WithoutBusinessHours_ReturnsBadRequest() throws Exception {
        UUID ownerId = createTestOwner();
        CreateRestaurantDTO restaurantDTO = createRestaurantDTO(ownerId);
        restaurantDTO.setBusinessHours(null);

        mockMvc.perform(post("/api/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Business hours are required"));
    }

    @Test
    public void createRestaurant_WithInvalidBusinessHours_ReturnsBadRequest() throws Exception {
        UUID ownerId = createTestOwner();
        CreateRestaurantDTO restaurantDTO = createRestaurantDTO(ownerId);
        
        restaurantDTO.getBusinessHours().get(0).setOpeningTime(java.time.LocalTime.parse("18:00"));
        restaurantDTO.getBusinessHours().get(0).setClosingTime(java.time.LocalTime.parse("09:00"));

        mockMvc.perform(post("/api/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Closing time cannot be before opening time"));
    }
} 