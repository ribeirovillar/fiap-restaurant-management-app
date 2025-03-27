package fiap.restaurant.app.adapter.web.integration.usertype;

import fiap.restaurant.app.adapter.web.json.usertype.UserTypeRequestDTO;
import fiap.restaurant.app.adapter.web.json.usertype.UserTypeResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateUserTypeIntegrationTest extends BaseUserTypeIntegrationTest {

    @Test
    public void createUserType_WithValidData_ReturnsCreatedUserType() throws Exception {
        String typeName = generateUniqueName("TYPE_");
        
        UserTypeRequestDTO userTypeDTO = new UserTypeRequestDTO();
        userTypeDTO.setName(typeName);

        MvcResult result = mockMvc.perform(post("/api/v1/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userTypeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(typeName))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        UserTypeResponseDTO responseDTO = objectMapper.readValue(responseBody, UserTypeResponseDTO.class);

        assertNotNull(responseDTO.getId());
        assertEquals(typeName, responseDTO.getName());
    }

    @Test
    public void createUserType_WithDuplicateName_ReturnsBadRequest() throws Exception {
        String typeName = generateUniqueName("DUPLICATE_");
        
        UserTypeRequestDTO userTypeDTO = new UserTypeRequestDTO();
        userTypeDTO.setName(typeName);

        mockMvc.perform(post("/api/v1/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userTypeDTO)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userTypeDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User type with name " + typeName + " already exists"));
    }

    @Test
    public void createUserType_WithEmptyName_ReturnsBadRequest() throws Exception {
        UserTypeRequestDTO userTypeDTO = new UserTypeRequestDTO();
        userTypeDTO.setName("");

        mockMvc.perform(post("/api/v1/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userTypeDTO)))
                .andExpect(status().isBadRequest());
    }
} 