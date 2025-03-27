package fiap.restaurant.app.adapter.web.integration.usertype;

import fiap.restaurant.app.adapter.web.json.usertype.UserTypeResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteUserTypeIntegrationTest extends BaseUserTypeIntegrationTest {

    @Test
    public void deleteUserType_WithValidId_ReturnsNoContent() throws Exception {
        String typeName = generateUniqueName("DELETE_");
        UserTypeResponseDTO createdUserType = createUserTypeAndReturn(typeName);

        mockMvc.perform(delete("/api/v1/user-types/{id}", createdUserType.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/user-types/{id}", createdUserType.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User type with ID " + createdUserType.getId() + " not found"));
    }

    @Test
    public void deleteUserType_WithNonExistingId_ReturnsBadRequest() throws Exception {
        UUID randomId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/user-types/{id}", randomId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User type with ID " + randomId + " not found"));
    }
    
    @Test
    public void deleteUserType_WithSystemDefaultTypes_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/user-types/name/CUSTOMER"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    UserTypeResponseDTO customerType = objectMapper.readValue(responseBody, UserTypeResponseDTO.class);
                    
                    mockMvc.perform(delete("/api/v1/user-types/{id}", customerType.getId()))
                            .andExpect(status().isBadRequest());
                });
    }
} 