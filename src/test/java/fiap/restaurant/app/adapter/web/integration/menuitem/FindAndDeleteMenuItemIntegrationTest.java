package fiap.restaurant.app.adapter.web.integration.menuitem;

import fiap.restaurant.app.adapter.web.json.menuitem.MenuItemDTO;
import fiap.restaurant.app.adapter.web.json.menuitem.UpsertMenuItemDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class FindAndDeleteMenuItemIntegrationTest extends BaseMenuItemIntegrationTest {

    @Test
    public void findMenuItemById_WithExistingId_ReturnsMenuItem() throws Exception {
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
        
        mockMvc.perform(get("/api/v1/restaurants/" + restaurantId + "/menu-items/" + createdMenuItem.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdMenuItem.getId().toString()))
                .andExpect(jsonPath("$.name").value(createdMenuItem.getName()))
                .andExpect(jsonPath("$.description").value(createdMenuItem.getDescription()))
                .andExpect(jsonPath("$.price").value(createdMenuItem.getPrice().doubleValue()))
                .andExpect(jsonPath("$.availableForTakeout").value(createdMenuItem.isAvailableForTakeout()))
                .andExpect(jsonPath("$.photoPath").value(createdMenuItem.getPhotoPath()))
                .andExpect(jsonPath("$.restaurantId").value(restaurantId.toString()));
    }
    
    @Test
    public void findMenuItemById_WithNonExistentId_ReturnsNotFound() throws Exception {
        UUID ownerId = createTestOwner();
        UUID restaurantId = createTestRestaurant(ownerId);
        UUID nonExistentMenuItemId = UUID.randomUUID();
        
        mockMvc.perform(get("/api/v1/restaurants/" + restaurantId + "/menu-items/" + nonExistentMenuItemId))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void findMenuItemsByRestaurantId_WithExistingRestaurant_ReturnsMenuItems() throws Exception {
        UUID ownerId = createTestOwner();
        UUID restaurantId = createTestRestaurant(ownerId);
        
        List<UUID> menuItemIds = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            UpsertMenuItemDTO menuItemDTO = createMenuItemDTO();
            menuItemDTO.setName("Menu Item " + (i + 1));
            
            MvcResult result = mockMvc.perform(post("/api/v1/restaurants/" + restaurantId + "/menu-items")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(menuItemDTO)))
                    .andExpect(status().isOk())
                    .andReturn();
            
            MenuItemDTO createdMenuItem = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    MenuItemDTO.class
            );
            
            menuItemIds.add(createdMenuItem.getId());
        }
        
        MvcResult result = mockMvc.perform(get("/api/v1/restaurants/" + restaurantId + "/menu-items"))
                .andExpect(status().isOk())
                .andReturn();
        
        List<MenuItemDTO> menuItems = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, MenuItemDTO.class)
        );
        
        assertEquals(3, menuItems.size());
        assertTrue(menuItems.stream().anyMatch(item -> item.getId().equals(menuItemIds.get(0))));
        assertTrue(menuItems.stream().anyMatch(item -> item.getId().equals(menuItemIds.get(1))));
        assertTrue(menuItems.stream().anyMatch(item -> item.getId().equals(menuItemIds.get(2))));
        assertTrue(menuItems.stream().allMatch(item -> item.getRestaurantId().equals(restaurantId)));
    }
    
    @Test
    public void findMenuItemsByRestaurantId_WithNonExistentRestaurant_ReturnsEmptyList() throws Exception {
        UUID nonExistentRestaurantId = UUID.randomUUID();
        
        MvcResult result = mockMvc.perform(get("/api/v1/restaurants/" + nonExistentRestaurantId + "/menu-items"))
                .andExpect(status().isOk())
                .andReturn();
        
        List<MenuItemDTO> menuItems = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, MenuItemDTO.class)
        );
        
        assertTrue(menuItems.isEmpty());
    }
    
    @Test
    public void deleteMenuItem_WithExistingId_ReturnsNoContent() throws Exception {
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
        
        mockMvc.perform(delete("/api/v1/restaurants/" + restaurantId + "/menu-items/" + createdMenuItem.getId()))
                .andExpect(status().isNoContent());
        
        mockMvc.perform(get("/api/v1/restaurants/" + restaurantId + "/menu-items/" + createdMenuItem.getId()))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void deleteMenuItem_WithNonExistentId_ReturnsBadRequest() throws Exception {
        UUID ownerId = createTestOwner();
        UUID restaurantId = createTestRestaurant(ownerId);
        UUID nonExistentMenuItemId = UUID.randomUUID();
        
        mockMvc.perform(delete("/api/v1/restaurants/" + restaurantId + "/menu-items/" + nonExistentMenuItemId))
                .andExpect(status().isBadRequest());
    }
} 