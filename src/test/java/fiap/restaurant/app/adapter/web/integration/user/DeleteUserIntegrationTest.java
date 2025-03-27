package fiap.restaurant.app.adapter.web.integration.user;

import fiap.restaurant.app.adapter.web.json.user.CreateUserDTO;
import fiap.restaurant.app.adapter.web.json.user.UserResponseDTO;
import fiap.restaurant.app.util.UserTypeTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteUserIntegrationTest extends BaseUserIntegrationTest {

    @Test
    public void deleteUser_WithValidId_ReturnsNoContent() throws Exception {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName("User To Delete");
        userDTO.setEmail("delete-user@example.com");
        userDTO.setLogin("deleteuser" + System.currentTimeMillis());
        userDTO.setPassword("password123");
        userDTO.setUserType(UserTypeTestHelper.createCustomerDTO());
        userDTO.setAddress(createAddressDTO());
        
        MvcResult createResult = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andReturn();
        
        UserResponseDTO createdUser = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                UserResponseDTO.class
        );
        
        UUID userId = createdUser.getId();
        
        mockMvc.perform(get("/api/v1/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()));
        
        mockMvc.perform(delete("/api/v1/users/" + userId))
                .andExpect(status().isNoContent());
        
        mockMvc.perform(get("/api/v1/users/" + userId))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void deleteUser_WithInvalidId_ReturnsNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        
        mockMvc.perform(delete("/api/v1/users/" + randomId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found with id: " + randomId));
    }
} 