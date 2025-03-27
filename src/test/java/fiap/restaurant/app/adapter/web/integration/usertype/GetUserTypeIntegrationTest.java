package fiap.restaurant.app.adapter.web.integration.usertype;

import com.fasterxml.jackson.core.type.TypeReference;
import fiap.restaurant.app.adapter.web.json.usertype.UserTypeResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetUserTypeIntegrationTest extends BaseUserTypeIntegrationTest {

    @Test
    public void getAllUserTypes_ReturnsUserTypesList() throws Exception {
        String typeName1 = generateUniqueName("TYPE1_");
        String typeName2 = generateUniqueName("TYPE2_");
        
        UserTypeResponseDTO userType1 = createUserTypeAndReturn(typeName1);
        UserTypeResponseDTO userType2 = createUserTypeAndReturn(typeName2);

        MvcResult result = mockMvc.perform(get("/api/v1/user-types"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        List<UserTypeResponseDTO> responseList = objectMapper.readValue(responseBody, new TypeReference<>() {});

        assertNotNull(responseList);
        assertTrue(responseList.size() >= 2);
        
        boolean foundType1 = false;
        boolean foundType2 = false;
        
        for (UserTypeResponseDTO userType : responseList) {
            if (userType.getId().equals(userType1.getId())) {
                foundType1 = true;
                assertEquals(typeName1, userType.getName());
            } else if (userType.getId().equals(userType2.getId())) {
                foundType2 = true;
                assertEquals(typeName2, userType.getName());
            }
        }
        
        assertTrue(foundType1);
        assertTrue(foundType2);
    }

    @Test
    public void getUserTypeById_WithValidId_ReturnsUserType() throws Exception {
        String typeName = generateUniqueName("BYID_");
        UserTypeResponseDTO createdUserType = createUserTypeAndReturn(typeName);

        mockMvc.perform(get("/api/v1/user-types/{id}", createdUserType.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdUserType.getId().toString()))
                .andExpect(jsonPath("$.name").value(typeName));
    }

    @Test
    public void getUserTypeById_WithInvalidId_ReturnsNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();

        mockMvc.perform(get("/api/v1/user-types/{id}", randomId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User type with ID " + randomId + " not found"));
    }

    @Test
    public void getUserTypeByName_WithValidName_ReturnsUserType() throws Exception {
        String typeName = generateUniqueName("BYNAME_");
        UserTypeResponseDTO createdUserType = createUserTypeAndReturn(typeName);

        mockMvc.perform(get("/api/v1/user-types/name/{name}", typeName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdUserType.getId().toString()))
                .andExpect(jsonPath("$.name").value(typeName));
    }

    @Test
    public void getUserTypeByName_WithInvalidName_ReturnsNotFound() throws Exception {
        String nonExistingName = "NON_EXISTING_TYPE_" + System.currentTimeMillis();

        mockMvc.perform(get("/api/v1/user-types/name/{name}", nonExistingName))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User type with name " + nonExistingName + " not found"));
    }
} 