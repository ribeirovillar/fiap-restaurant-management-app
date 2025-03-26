package fiap.restaurant.app.adapter.web.integration.menuitem;

import fiap.restaurant.app.adapter.web.json.menuitem.MenuItemDTO;
import fiap.restaurant.app.adapter.web.json.menuitem.UpsertMenuItemDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UpdateMenuItemIntegrationTest extends BaseMenuItemIntegrationTest {

    @Test
    public void updateMenuItem_WithValidData_ReturnsUpdatedMenuItem() throws Exception {
        UUID ownerId = createTestOwner();
        UUID restaurantId = createTestRestaurant(ownerId);
        
        UpsertMenuItemDTO createMenuItemDTO = createMenuItemDTO();
        MvcResult createResult = mockMvc.perform(post("/api/v1/restaurants/" + restaurantId + "/menu-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMenuItemDTO)))
                .andExpect(status().isOk())
                .andReturn();
        
        MenuItemDTO createdMenuItem = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                MenuItemDTO.class
        );
        
        UpsertMenuItemDTO updateMenuItemDTO = new UpsertMenuItemDTO();
        updateMenuItemDTO.setName("Updated Menu Item");
        updateMenuItemDTO.setDescription("This is an updated description for the menu item that is sufficiently long");
        updateMenuItemDTO.setPrice(new BigDecimal("18.99"));
        updateMenuItemDTO.setAvailableForTakeout(false);
        updateMenuItemDTO.setPhotoPath("/images/updated.jpg");
        
        MvcResult updateResult = mockMvc.perform(put("/api/v1/restaurants/" + restaurantId + "/menu-items/" + createdMenuItem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateMenuItemDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updateMenuItemDTO.getName()))
                .andExpect(jsonPath("$.description").value(updateMenuItemDTO.getDescription()))
                .andExpect(jsonPath("$.price").value(updateMenuItemDTO.getPrice().doubleValue()))
                .andExpect(jsonPath("$.availableForTakeout").value(updateMenuItemDTO.isAvailableForTakeout()))
                .andExpect(jsonPath("$.photoPath").value(updateMenuItemDTO.getPhotoPath()))
                .andExpect(jsonPath("$.restaurantId").value(restaurantId.toString()))
                .andExpect(jsonPath("$.id").value(createdMenuItem.getId().toString()))
                .andReturn();
        
        String responseBody = updateResult.getResponse().getContentAsString();
        MenuItemDTO updatedMenuItemDTO = objectMapper.readValue(responseBody, MenuItemDTO.class);
        
        assertEquals(createdMenuItem.getId(), updatedMenuItemDTO.getId());
        assertEquals(updateMenuItemDTO.getName(), updatedMenuItemDTO.getName());
        assertEquals(updateMenuItemDTO.getDescription(), updatedMenuItemDTO.getDescription());
        assertEquals(updateMenuItemDTO.getPrice().doubleValue(), updatedMenuItemDTO.getPrice().doubleValue());
        assertEquals(updateMenuItemDTO.isAvailableForTakeout(), updatedMenuItemDTO.isAvailableForTakeout());
        assertEquals(updateMenuItemDTO.getPhotoPath(), updatedMenuItemDTO.getPhotoPath());
        assertEquals(restaurantId, updatedMenuItemDTO.getRestaurantId());
        assertEquals(createdMenuItem.getCreatedAt(), updatedMenuItemDTO.getCreatedAt());
        assertNotEquals(createdMenuItem.getUpdatedAt(), updatedMenuItemDTO.getUpdatedAt());
    }
    
    @Test
    public void updateMenuItem_WithNonExistentMenuItem_ReturnsBadRequest() throws Exception {
        UUID ownerId = createTestOwner();
        UUID restaurantId = createTestRestaurant(ownerId);
        UUID nonExistentMenuItemId = UUID.randomUUID();
        
        UpsertMenuItemDTO updateMenuItemDTO = createMenuItemDTO();
        
        mockMvc.perform(put("/api/v1/restaurants/" + restaurantId + "/menu-items/" + nonExistentMenuItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateMenuItemDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Menu item not found"));
    }
    
    @Test
    public void updateMenuItem_WithNonExistentRestaurant_ReturnsBadRequest() throws Exception {
        UUID nonExistentRestaurantId = UUID.randomUUID();
        UUID menuItemId = UUID.randomUUID();
        
        UpsertMenuItemDTO updateMenuItemDTO = createMenuItemDTO();
        
        mockMvc.perform(put("/api/v1/restaurants/" + nonExistentRestaurantId + "/menu-items/" + menuItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateMenuItemDTO)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    public void updateMenuItem_WithInvalidData_ReturnsBadRequest() throws Exception {
        UUID ownerId = createTestOwner();
        UUID restaurantId = createTestRestaurant(ownerId);
        
        UpsertMenuItemDTO createMenuItemDTO = createMenuItemDTO();
        MvcResult createResult = mockMvc.perform(post("/api/v1/restaurants/" + restaurantId + "/menu-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMenuItemDTO)))
                .andExpect(status().isOk())
                .andReturn();
        
        MenuItemDTO createdMenuItem = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                MenuItemDTO.class
        );
        
        UpsertMenuItemDTO updateMenuItemDTO = new UpsertMenuItemDTO();
        updateMenuItemDTO.setName("AB");
        updateMenuItemDTO.setDescription("This is an updated description for the menu item that is sufficiently long");
        updateMenuItemDTO.setPrice(new BigDecimal("18.99"));
        updateMenuItemDTO.setAvailableForTakeout(false);
        updateMenuItemDTO.setPhotoPath("/images/updated.jpg");
        
        mockMvc.perform(put("/api/v1/restaurants/" + restaurantId + "/menu-items/" + createdMenuItem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateMenuItemDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Name must be between 3 and 100 characters"));
    }
} 