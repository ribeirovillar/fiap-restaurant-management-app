package fiap.restaurant.app.adapter.web.integration.user;

import fiap.restaurant.app.adapter.web.json.user.CreateUserDTO;
import fiap.restaurant.app.adapter.web.json.user.LoginRequestDTO;
import fiap.restaurant.app.adapter.web.json.user.UpdatePasswordDTO;
import fiap.restaurant.app.adapter.web.json.user.UserResponseDTO;
import fiap.restaurant.app.util.UserTypeTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdatePasswordIntegrationTest extends BaseUserIntegrationTest {

    @Test
    public void updatePassword_WithValidCredentials_ReturnsSuccess() throws Exception {
        String login = "updatepwd" + System.currentTimeMillis();
        String originalPassword = "password123";
        
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setName("Password Update User");
        createUserDTO.setEmail("pwd-update@example.com");
        createUserDTO.setLogin(login);
        createUserDTO.setPassword(originalPassword);
        createUserDTO.setUserType(UserTypeTestHelper.createCustomerDTO());
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
        
        assertNotNull(createdUser.getId());
        
        String newPassword = "newPassword456";
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setLogin(login);
        updatePasswordDTO.setCurrentPassword(originalPassword);
        updatePasswordDTO.setNewPassword(newPassword);
        
        mockMvc.perform(post("/api/v1/users/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePasswordDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password updated successfully"))
                .andExpect(jsonPath("$.success").value(true));
        
        LoginRequestDTO oldLoginRequest = new LoginRequestDTO();
        oldLoginRequest.setLogin(login);
        oldLoginRequest.setPassword(originalPassword);
        
        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(oldLoginRequest)))
                .andExpect(status().isUnauthorized());
        
        LoginRequestDTO newLoginRequest = new LoginRequestDTO();
        newLoginRequest.setLogin(login);
        newLoginRequest.setPassword(newPassword);
        
        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login success"))
                .andExpect(jsonPath("$.success").value(true));
    }
    
    @Test
    public void updatePassword_WithInvalidCurrentPassword_ReturnsUnauthorized() throws Exception {
        String login = "badpwd" + System.currentTimeMillis();
        String originalPassword = "password123";
        
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setName("Invalid Password User");
        createUserDTO.setEmail("invalid-pwd@example.com");
        createUserDTO.setLogin(login);
        createUserDTO.setPassword(originalPassword);
        createUserDTO.setUserType(UserTypeTestHelper.createCustomerDTO());
        createUserDTO.setAddress(createAddressDTO());
        
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isCreated());
        
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setLogin(login);
        updatePasswordDTO.setCurrentPassword("wrongPassword");
        updatePasswordDTO.setNewPassword("newPassword456");
        
        mockMvc.perform(post("/api/v1/users/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePasswordDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Current password is incorrect"));
        
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setLogin(login);
        loginRequest.setPassword(originalPassword);
        
        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
    
    @Test
    public void updatePassword_WithInvalidLogin_ReturnsUnauthorized() throws Exception {
        String nonExistentLogin = "nonexistentuser" + System.currentTimeMillis();
        
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setLogin(nonExistentLogin);
        updatePasswordDTO.setCurrentPassword("anyPassword");
        updatePasswordDTO.setNewPassword("newPassword");
        
        mockMvc.perform(post("/api/v1/users/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePasswordDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Current password is incorrect"));
    }
    
    @Test
    public void updatePassword_WithMissingFields_ReturnsBadRequest() throws Exception {
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();

        mockMvc.perform(post("/api/v1/users/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePasswordDTO)))
                .andExpect(status().isBadRequest());
        
        updatePasswordDTO.setLogin("somelogin");
        
        mockMvc.perform(post("/api/v1/users/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePasswordDTO)))
                .andExpect(status().isBadRequest());
        
        updatePasswordDTO.setCurrentPassword("currentpwd");
        
        mockMvc.perform(post("/api/v1/users/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePasswordDTO)))
                .andExpect(status().isBadRequest());
    }
} 