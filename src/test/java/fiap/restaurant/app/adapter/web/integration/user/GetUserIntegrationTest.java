package fiap.restaurant.app.adapter.web.integration.user;

import com.fasterxml.jackson.core.type.TypeReference;
import fiap.restaurant.app.adapter.web.json.AddressDTO;
import fiap.restaurant.app.adapter.web.json.CreateUserDTO;
import fiap.restaurant.app.adapter.web.json.UserResponseDTO;
import fiap.restaurant.app.core.domain.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetUserIntegrationTest extends BaseUserIntegrationTest {

    @Test
    public void getAllUsers_ReturnsListOfUsers() throws Exception {
        CreateUserDTO user1 = new CreateUserDTO();
        user1.setName("User One");
        user1.setEmail("user.one@example.com");
        user1.setLogin("userone" + System.currentTimeMillis());
        user1.setPassword("password123");
        user1.setUserType(UserType.CUSTOMER);
        
        AddressDTO address1 = createAddressDTO();
        address1.setStreet("First Street");
        address1.setCity("First City");
        address1.setState("FS");
        user1.setAddress(address1);
        
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isCreated());
        
        CreateUserDTO user2 = new CreateUserDTO();
        user2.setName("User Two");
        user2.setEmail("user.two@example.com");
        user2.setLogin("usertwo" + System.currentTimeMillis());
        user2.setPassword("password456");
        user2.setUserType(UserType.OWNER);
        
        AddressDTO address2 = createAddressDTO();
        address2.setStreet("Second Street");
        address2.setCity("Second City");
        address2.setState("SS");
        user2.setAddress(address2);
        
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().isCreated());
        
        MvcResult result = mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andReturn();
        
        String responseBody = result.getResponse().getContentAsString();
        List<UserResponseDTO> userList = objectMapper.readValue(responseBody, new TypeReference<List<UserResponseDTO>>() {});
        
        assertFalse(userList.isEmpty());
        
        assertTrue(userList.size() >= 2);
        
        boolean foundUser1 = false;
        boolean foundUser2 = false;
        
        for (UserResponseDTO user : userList) {
            if (user.getEmail().equals(user1.getEmail())) {
                foundUser1 = true;
                assertEquals(user1.getName(), user.getName());
                assertEquals(user1.getLogin(), user.getLogin());
                assertEquals(user1.getUserType(), user.getUserType());
            } else if (user.getEmail().equals(user2.getEmail())) {
                foundUser2 = true;
                assertEquals(user2.getName(), user.getName());
                assertEquals(user2.getLogin(), user.getLogin());
                assertEquals(user2.getUserType(), user.getUserType());
            }
        }
        
        assertTrue(foundUser1, "Usuário 1 não encontrado na lista");
        assertTrue(foundUser2, "Usuário 2 não encontrado na lista");
    }
    
    @Test
    public void getUserById_WithValidId_ReturnsUser() throws Exception {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName("Get By ID User");
        userDTO.setEmail("getbyid@example.com");
        userDTO.setLogin("getbyid" + System.currentTimeMillis());
        userDTO.setPassword("password123");
        userDTO.setUserType(UserType.CUSTOMER);
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
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value(userDTO.getName()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()))
                .andExpect(jsonPath("$.login").value(userDTO.getLogin()))
                .andExpect(jsonPath("$.userType").value(userDTO.getUserType().toString()));
    }
    
    @Test
    public void getUserById_WithInvalidId_ReturnsNotFound() throws Exception {
        UUID randomId = UUID.randomUUID();
        
        mockMvc.perform(get("/api/v1/users/" + randomId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found with id: " + randomId));
    }
} 