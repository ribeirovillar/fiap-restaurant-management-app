package fiap.restaurant.app.adapter.web.integration.usertype;

import fiap.restaurant.app.adapter.web.json.usertype.UserTypeRequestDTO;
import fiap.restaurant.app.adapter.web.json.usertype.UserTypeResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateUserTypeIntegrationTest extends BaseUserTypeIntegrationTest {

    @Test
    public void updateUserType_WithValidData_ReturnsUpdatedUserType() throws Exception {
        String originalName = generateUniqueName("ORIGINAL_");
        String updatedName = generateUniqueName("UPDATED_");

        UserTypeResponseDTO createdUserType = createUserTypeAndReturn(originalName);

        UserTypeRequestDTO updateUserTypeDTO = new UserTypeRequestDTO();
        updateUserTypeDTO.setName(updatedName);

        mockMvc.perform(put("/api/v1/user-types/{id}", createdUserType.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserTypeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdUserType.getId().toString()))
                .andExpect(jsonPath("$.name").value(updatedName));
    }

    @Test
    public void updateUserType_WithNonExistingId_ReturnsBadRequest() throws Exception {
        UUID randomId = UUID.randomUUID();

        UserTypeRequestDTO updateUserTypeDTO = new UserTypeRequestDTO();
        updateUserTypeDTO.setName(generateUniqueName("NONEXISTING_"));

        mockMvc.perform(put("/api/v1/user-types/{id}", randomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserTypeDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User type with ID " + randomId + " not found"));
    }

    @Test
    public void updateUserType_WithDuplicateName_ReturnsBadRequest() throws Exception {
        String name1 = generateUniqueName("FIRST_");
        String name2 = generateUniqueName("SECOND_");

        createUserTypeAndReturn(name1);
        UserTypeResponseDTO userType2 = createUserTypeAndReturn(name2);

        UserTypeRequestDTO updateUserTypeDTO = new UserTypeRequestDTO();
        updateUserTypeDTO.setName(name1);

        mockMvc.perform(put("/api/v1/user-types/{id}", userType2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserTypeDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User type with name " + name1 + " already exists"));
    }

    @Test
    public void updateUserType_WithEmptyName_ReturnsBadRequest() throws Exception {
        UserTypeResponseDTO createdUserType = createUserTypeAndReturn(generateUniqueName("EMPTY_"));

        UserTypeRequestDTO updateUserTypeDTO = new UserTypeRequestDTO();
        updateUserTypeDTO.setName("");

        mockMvc.perform(put("/api/v1/user-types/{id}", createdUserType.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserTypeDTO)))
                .andExpect(status().isBadRequest());
    }
} 