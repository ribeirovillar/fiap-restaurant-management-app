package fiap.restaurant.app.adapter.web.integration.user;

import fiap.restaurant.app.adapter.web.json.AddressDTO;
import fiap.restaurant.app.adapter.web.json.CreateUserDTO;
import fiap.restaurant.app.adapter.web.json.UpdateUserDTO;
import fiap.restaurant.app.adapter.web.json.UserResponseDTO;
import fiap.restaurant.app.core.domain.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateUserIntegrationTest extends BaseUserIntegrationTest {

    @Test
    public void updateUser_WithValidData_ReturnsUpdatedUser() throws Exception {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setName("User To Update");
        createUserDTO.setEmail("update-user@example.com");
        createUserDTO.setLogin("updateuser" + System.currentTimeMillis());
        createUserDTO.setPassword("password123");
        createUserDTO.setUserType(UserType.CUSTOMER);
        createUserDTO.setAddress(createAddressDTO());
        
        MvcResult createResult = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isCreated())
                .andReturn();
        
        UserResponseDTO createdUser = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                UserResponseDTO.class
        );
        
        UUID userId = createdUser.getId();
        
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setName("User Updated Name");
        updateUserDTO.setEmail("updated-email@example.com");
        updateUserDTO.setUserType(UserType.OWNER);
        
        AddressDTO updatedAddress = new AddressDTO();
        updatedAddress.setStreet("Updated Street");
        updatedAddress.setCity("Updated City");
        updatedAddress.setState("US");
        updatedAddress.setZipCode("54321");
        updatedAddress.setCountry("Updated Country");
        updateUserDTO.setAddress(updatedAddress);
        
        MvcResult updateResult = mockMvc.perform(put("/api/v1/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value(updateUserDTO.getName()))
                .andExpect(jsonPath("$.email").value(updateUserDTO.getEmail()))
                .andExpect(jsonPath("$.userType").value(updateUserDTO.getUserType().toString()))
                .andExpect(jsonPath("$.login").value(createdUser.getLogin()))
                .andReturn();
        
        UserResponseDTO updatedUser = objectMapper.readValue(
                updateResult.getResponse().getContentAsString(),
                UserResponseDTO.class
        );
        
        assertEquals(userId, updatedUser.getId());
        assertEquals(updateUserDTO.getName(), updatedUser.getName());
        assertEquals(updateUserDTO.getEmail(), updatedUser.getEmail());
        assertEquals(updateUserDTO.getUserType(), updatedUser.getUserType());
        assertEquals(createdUser.getLogin(), updatedUser.getLogin());
        
        assertNotNull(updatedUser.getAddress());
        assertEquals(updatedAddress.getStreet(), updatedUser.getAddress().getStreet());
        assertEquals(updatedAddress.getCity(), updatedUser.getAddress().getCity());
        assertEquals(updatedAddress.getState(), updatedUser.getAddress().getState());
        assertEquals(updatedAddress.getZipCode(), updatedUser.getAddress().getZipCode());
        assertEquals(updatedAddress.getCountry(), updatedUser.getAddress().getCountry());
        
        mockMvc.perform(get("/api/v1/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value(updateUserDTO.getName()))
                .andExpect(jsonPath("$.email").value(updateUserDTO.getEmail()))
                .andExpect(jsonPath("$.userType").value(updateUserDTO.getUserType().toString()))
                .andExpect(jsonPath("$.login").value(createdUser.getLogin()));
    }
    
    @Test
    public void updateUser_WithInvalidId_ReturnsNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setName("Updated Name");
        updateUserDTO.setEmail("updated@example.com");
        updateUserDTO.setUserType(UserType.CUSTOMER);
        updateUserDTO.setAddress(createAddressDTO());
        
        mockMvc.perform(put("/api/v1/users/" + randomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found with id: " + randomId));
    }
    
    @Test
    public void updateUser_WithExistingEmail_ReturnsConflict() throws Exception {
        CreateUserDTO firstUser = new CreateUserDTO();
        firstUser.setName("First User");
        firstUser.setEmail("first.email@example.com");
        firstUser.setLogin("firstupdate" + System.currentTimeMillis());
        firstUser.setPassword("password123");
        firstUser.setUserType(UserType.CUSTOMER);
        firstUser.setAddress(createAddressDTO());
        
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstUser)))
                .andExpect(status().isCreated());
        
        CreateUserDTO secondUser = new CreateUserDTO();
        secondUser.setName("Second User");
        secondUser.setEmail("second.email@example.com");
        secondUser.setLogin("secondupdate" + System.currentTimeMillis());
        secondUser.setPassword("password456");
        secondUser.setUserType(UserType.CUSTOMER);
        secondUser.setAddress(createAddressDTO());
        
        MvcResult secondCreateResult = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(secondUser)))
                .andExpect(status().isCreated())
                .andReturn();
        
        UserResponseDTO secondCreatedUser = objectMapper.readValue(
                secondCreateResult.getResponse().getContentAsString(),
                UserResponseDTO.class
        );
        
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setName("Updated Second User");
        updateUserDTO.setEmail("first.email@example.com");
        updateUserDTO.setUserType(UserType.OWNER);
        updateUserDTO.setAddress(createAddressDTO());
        
        mockMvc.perform(put("/api/v1/users/" + secondCreatedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already in use by another user: first.email@example.com"))
                .andExpect(jsonPath("$.status").value(409));
    }
    
    @Test
    public void updateUser_WithInvalidEmail_ReturnsBadRequest() throws Exception {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setName("Email Validation User");
        createUserDTO.setEmail("email-validation@example.com");
        createUserDTO.setLogin("emailvalidation" + System.currentTimeMillis());
        createUserDTO.setPassword("password123");
        createUserDTO.setUserType(UserType.CUSTOMER);
        createUserDTO.setAddress(createAddressDTO());
        
        MvcResult createResult = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isCreated())
                .andReturn();
        
        UserResponseDTO createdUser = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                UserResponseDTO.class
        );
        
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setName("Updated Name");
        updateUserDTO.setEmail("invalid-email");
        updateUserDTO.setUserType(UserType.CUSTOMER);
        updateUserDTO.setAddress(createAddressDTO());
        
        mockMvc.perform(put("/api/v1/users/" + createdUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid email format: invalid-email"));
    }
} 