package fiap.restaurant.app.adapter.web.integration.user;

import fiap.restaurant.app.adapter.web.json.user.AddressDTO;
import fiap.restaurant.app.adapter.web.json.user.CreateUserDTO;
import fiap.restaurant.app.adapter.web.json.user.UserResponseDTO;
import fiap.restaurant.app.core.domain.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateUserIntegrationTest extends BaseUserIntegrationTest {

    @Test
    public void createUser_WithValidData_ReturnsCreatedUser() throws Exception {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName("Test User");
        userDTO.setEmail("testuser@example.com");
        userDTO.setLogin("testuser" + System.currentTimeMillis());
        userDTO.setPassword("password123");
        userDTO.setUserType(UserType.CUSTOMER);

        AddressDTO addressDTO = createAddressDTO();
        userDTO.setAddress(addressDTO);

        MvcResult result = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(userDTO.getName()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()))
                .andExpect(jsonPath("$.login").value(userDTO.getLogin()))
                .andExpect(jsonPath("$.userType").value(userDTO.getUserType().toString()))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        UserResponseDTO responseDTO = objectMapper.readValue(responseBody, UserResponseDTO.class);

        assertNotNull(responseDTO.getId());
        assertNotNull(responseDTO.getLastModifiedDate());
        assertEquals(userDTO.getName(), responseDTO.getName());
        assertEquals(userDTO.getEmail(), responseDTO.getEmail());
        assertEquals(userDTO.getLogin(), responseDTO.getLogin());
        assertEquals(userDTO.getUserType(), responseDTO.getUserType());

        assertNotNull(responseDTO.getAddress());
        assertEquals(addressDTO.getStreet(), responseDTO.getAddress().getStreet());
        assertEquals(addressDTO.getCity(), responseDTO.getAddress().getCity());
        assertEquals(addressDTO.getState(), responseDTO.getAddress().getState());
        assertEquals(addressDTO.getZipCode(), responseDTO.getAddress().getZipCode());
        assertEquals(addressDTO.getCountry(), responseDTO.getAddress().getCountry());
    }

    @Test
    public void createUser_WithInvalidEmail_ReturnsBadRequest() throws Exception {
        CreateUserDTO userDTO = createUserDTO("Invalid Email User", "invalid-email", "invaliduser");

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid email format: invalid-email"));
    }

    @Test
    public void createUser_WithDuplicateLogin_ReturnsConflict() throws Exception {
        String sharedLogin = "duplicateuser" + System.currentTimeMillis();

        CreateUserDTO firstUser = new CreateUserDTO();
        firstUser.setName("First User");
        firstUser.setEmail("firstuser@example.com");
        firstUser.setLogin(sharedLogin);
        firstUser.setPassword("password123");
        firstUser.setUserType(UserType.CUSTOMER);
        firstUser.setAddress(createAddressDTO());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstUser)))
                .andExpect(status().isCreated());

        CreateUserDTO secondUser = new CreateUserDTO();
        secondUser.setName("Second User");
        secondUser.setEmail("seconduser@example.com");
        secondUser.setLogin(sharedLogin);
        secondUser.setPassword("password456");
        secondUser.setUserType(UserType.OWNER);
        secondUser.setAddress(createAddressDTO());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondUser)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Login already exists: " + sharedLogin))
                .andExpect(jsonPath("$.status").value(409));
    }
    
    @Test
    public void createUser_WithDuplicateEmail_ReturnsConflict() throws Exception {
        String sharedEmail = "shared.email" + System.currentTimeMillis() + "@example.com";
        
        CreateUserDTO firstUser = new CreateUserDTO();
        firstUser.setName("First User");
        firstUser.setEmail(sharedEmail);
        firstUser.setLogin("firstlogin" + System.currentTimeMillis());
        firstUser.setPassword("password123");
        firstUser.setUserType(UserType.CUSTOMER);
        firstUser.setAddress(createAddressDTO());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstUser)))
                .andExpect(status().isCreated());

        CreateUserDTO secondUser = new CreateUserDTO();
        secondUser.setName("Second User");
        secondUser.setEmail(sharedEmail);
        secondUser.setLogin("secondlogin" + System.currentTimeMillis());
        secondUser.setPassword("password456");
        secondUser.setUserType(UserType.OWNER);
        secondUser.setAddress(createAddressDTO());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondUser)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already exists: " + sharedEmail))
                .andExpect(jsonPath("$.status").value(409));
    }
} 