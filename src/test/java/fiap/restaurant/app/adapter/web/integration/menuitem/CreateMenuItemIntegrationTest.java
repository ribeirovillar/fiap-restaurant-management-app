package fiap.restaurant.app.adapter.web.integration.menuitem;

import fiap.restaurant.app.adapter.web.json.menuitem.MenuItemDTO;
import fiap.restaurant.app.adapter.web.json.menuitem.UpsertMenuItemDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateMenuItemIntegrationTest extends BaseMenuItemIntegrationTest {

    @Test
    public void createMenuItem_WithValidData_ReturnsCreatedMenuItem() throws Exception {
        UUID ownerId = createTestOwner();
        UUID restaurantId = createTestRestaurant(ownerId);
        UpsertMenuItemDTO menuItemDTO = createMenuItemDTO();

        MvcResult result = mockMvc.perform(post("/api/v1/restaurants/" + restaurantId + "/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItemDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(menuItemDTO.getName()))
                .andExpect(jsonPath("$.description").value(menuItemDTO.getDescription()))
                .andExpect(jsonPath("$.price").value(menuItemDTO.getPrice().doubleValue()))
                .andExpect(jsonPath("$.availableForTakeout").value(menuItemDTO.isAvailableForTakeout()))
                .andExpect(jsonPath("$.photoPath").value(menuItemDTO.getPhotoPath()))
                .andExpect(jsonPath("$.restaurantId").value(restaurantId.toString()))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        MenuItemDTO responseDTO = objectMapper.readValue(responseBody, MenuItemDTO.class);

        assertNotNull(responseDTO.getId());
        assertNotNull(responseDTO.getCreatedAt());
        assertNotNull(responseDTO.getUpdatedAt());
        assertEquals(menuItemDTO.getName(), responseDTO.getName());
        assertEquals(menuItemDTO.getDescription(), responseDTO.getDescription());
        assertEquals(menuItemDTO.getPrice().doubleValue(), responseDTO.getPrice().doubleValue());
        assertEquals(menuItemDTO.isAvailableForTakeout(), responseDTO.isAvailableForTakeout());
        assertEquals(menuItemDTO.getPhotoPath(), responseDTO.getPhotoPath());
        assertEquals(restaurantId, responseDTO.getRestaurantId());
    }

    @Test
    public void createMenuItem_WithInvalidRestaurant_ReturnsBadRequest() throws Exception {
        UUID nonExistentRestaurantId = UUID.randomUUID();
        UpsertMenuItemDTO menuItemDTO = createMenuItemDTO();

        mockMvc.perform(post("/api/v1/restaurants/" + nonExistentRestaurantId + "/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItemDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Restaurant not found"));
    }

    @Test
    public void createMenuItem_WithShortName_ReturnsBadRequest() throws Exception {
        UUID ownerId = createTestOwner();
        UUID restaurantId = createTestRestaurant(ownerId);
        UpsertMenuItemDTO menuItemDTO = createMenuItemDTO();
        menuItemDTO.setName("AB");

        mockMvc.perform(post("/api/v1/restaurants/" + restaurantId + "/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItemDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Name must be between 3 and 100 characters"));
    }

    @Test
    public void createMenuItem_WithShortDescription_ReturnsBadRequest() throws Exception {
        UUID ownerId = createTestOwner();
        UUID restaurantId = createTestRestaurant(ownerId);
        UpsertMenuItemDTO menuItemDTO = createMenuItemDTO();
        menuItemDTO.setDescription("Short");

        mockMvc.perform(post("/api/v1/restaurants/" + restaurantId + "/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItemDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Description must be between 10 and 500 characters"));
    }

    @Test
    public void createMenuItem_WithInvalidPrice_ReturnsBadRequest() throws Exception {
        UUID ownerId = createTestOwner();
        UUID restaurantId = createTestRestaurant(ownerId);
        UpsertMenuItemDTO menuItemDTO = createMenuItemDTO();
        menuItemDTO.setPrice(new BigDecimal("0.00"));

        mockMvc.perform(post("/api/v1/restaurants/" + restaurantId + "/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItemDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Price must be greater than 0.01"));
    }

    @Test
    public void createMenuItem_WithNullPhotoPath_ReturnsBadRequest() throws Exception {
        UUID ownerId = createTestOwner();
        UUID restaurantId = createTestRestaurant(ownerId);
        UpsertMenuItemDTO menuItemDTO = createMenuItemDTO();
        menuItemDTO.setPhotoPath(null);

        mockMvc.perform(post("/api/v1/restaurants/" + restaurantId + "/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItemDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Photo path is required"));
    }
} 