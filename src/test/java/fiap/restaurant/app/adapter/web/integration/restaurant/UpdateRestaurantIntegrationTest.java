package fiap.restaurant.app.adapter.web.integration.restaurant;

import fiap.restaurant.app.adapter.web.json.restaurant.CreateRestaurantDTO;
import fiap.restaurant.app.adapter.web.json.restaurant.RestaurantDTO;
import fiap.restaurant.app.adapter.web.json.restaurant.UpdateRestaurantDTO;
import fiap.restaurant.app.core.domain.CuisineType;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateRestaurantIntegrationTest extends BaseRestaurantIntegrationTest {

    @Test
    public void updateRestaurant_WithValidData_ReturnsUpdatedRestaurant() throws Exception {
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
        
        UpdateRestaurantDTO updateDTO = new UpdateRestaurantDTO();
        updateDTO.setName("Updated Restaurant Name");
        updateDTO.setCuisineType(CuisineType.MEXICAN);
        updateDTO.setAddress(createAddressDTO());
        updateDTO.getAddress().setCity("Updated City");
        updateDTO.setBusinessHours(List.of(
            createBusinessHourDTO(DayOfWeek.MONDAY, "10:00", "22:00", false),
            createBusinessHourDTO(DayOfWeek.TUESDAY, "10:00", "22:00", false)
        ));
        
        MvcResult updateResult = mockMvc.perform(put("/api/v1/restaurants/{id}", createdRestaurant.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updateDTO.getName()))
                .andExpect(jsonPath("$.cuisineType").value(updateDTO.getCuisineType().toString()))
                .andExpect(jsonPath("$.address.city").value(updateDTO.getAddress().getCity()))
                .andReturn();
        
        RestaurantDTO updatedRestaurant = objectMapper.readValue(
                updateResult.getResponse().getContentAsString(),
                RestaurantDTO.class);
        
        assertEquals(createdRestaurant.getId(), updatedRestaurant.getId());
        assertEquals(updateDTO.getName(), updatedRestaurant.getName());
        assertEquals(updateDTO.getCuisineType(), updatedRestaurant.getCuisineType());
        assertEquals(updateDTO.getAddress().getCity(), updatedRestaurant.getAddress().getCity());
        assertEquals(ownerId, updatedRestaurant.getOwnerId());
        assertEquals(2, updatedRestaurant.getBusinessHours().size());
        assertNotNull(updatedRestaurant.getUpdatedAt());
    }
    
    @Test
    public void updateRestaurant_WithInvalidId_ReturnsNotFound() throws Exception {
        UpdateRestaurantDTO updateDTO = new UpdateRestaurantDTO();
        updateDTO.setName("Updated Restaurant Name");
        updateDTO.setCuisineType(CuisineType.MEXICAN);
        
        mockMvc.perform(put("/api/v1/restaurants/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void updateRestaurant_WithInvalidData_ReturnsBadRequest() throws Exception {
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
        
        UpdateRestaurantDTO updateDTO = new UpdateRestaurantDTO();
        updateDTO.setName("AB");
        updateDTO.setCuisineType(CuisineType.MEXICAN);
        
        mockMvc.perform(put("/api/v1/restaurants/{id}", createdRestaurant.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Restaurant name must be at least 3 characters long"));
    }
    
    @Test
    public void updateRestaurant_WithInvalidBusinessHours_ReturnsBadRequest() throws Exception {
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
        
        UpdateRestaurantDTO updateDTO = new UpdateRestaurantDTO();
        updateDTO.setName("Updated Restaurant Name");
        updateDTO.setCuisineType(CuisineType.MEXICAN);
        updateDTO.setBusinessHours(List.of(
            createBusinessHourDTO(DayOfWeek.MONDAY, "18:00", "10:00", false) // Closing before opening
        ));
        
        mockMvc.perform(put("/api/v1/restaurants/{id}", createdRestaurant.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Closing time cannot be before opening time"));
    }
    
    @Test
    public void updateRestaurant_WithNonOwner_ReturnsUnauthorized() throws Exception {
        // Create first owner and restaurant
        UUID ownerId1 = createTestOwner();
        CreateRestaurantDTO createDTO = createRestaurantDTO(ownerId1);
        
        MvcResult createResult = mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andReturn();
                
        RestaurantDTO createdRestaurant = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                RestaurantDTO.class);


        UpdateRestaurantDTO updateDTO = new UpdateRestaurantDTO();
        updateDTO.setName("Updated Restaurant Name");
        updateDTO.setCuisineType(CuisineType.MEXICAN);

        mockMvc.perform(put("/api/v1/restaurants/{id}", createdRestaurant.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk());
    }
} 