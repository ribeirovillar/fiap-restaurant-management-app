package fiap.restaurant.app.adapter.web.integration.restaurant;

import fiap.restaurant.app.adapter.web.json.restaurant.CreateRestaurantDTO;
import fiap.restaurant.app.adapter.web.json.restaurant.RestaurantDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FindRestaurantByIdIntegrationTest extends BaseRestaurantIntegrationTest {

    @Test
    public void findRestaurantById_WithValidId_ReturnsRestaurant() throws Exception {
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
        
        MvcResult findResult = mockMvc.perform(get("/api/v1/restaurants/{id}", createdRestaurant.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdRestaurant.getId().toString()))
                .andExpect(jsonPath("$.name").value(createdRestaurant.getName()))
                .andExpect(jsonPath("$.cuisineType").value(createdRestaurant.getCuisineType().toString()))
                .andExpect(jsonPath("$.ownerId").value(ownerId.toString()))
                .andReturn();
        
        RestaurantDTO foundRestaurant = objectMapper.readValue(
                findResult.getResponse().getContentAsString(),
                RestaurantDTO.class);
        
        assertEquals(createdRestaurant.getId(), foundRestaurant.getId());
        assertEquals(createdRestaurant.getName(), foundRestaurant.getName());
        assertEquals(createdRestaurant.getCuisineType(), foundRestaurant.getCuisineType());
        assertEquals(ownerId, foundRestaurant.getOwnerId());
        assertNotNull(foundRestaurant.getAddress());
        assertNotNull(foundRestaurant.getBusinessHours());
    }
    
    @Test
    public void findRestaurantById_WithInvalidId_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/restaurants/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
} 