package fiap.restaurant.app.adapter.web.integration.user;

import fiap.restaurant.app.adapter.web.json.CreateUserDTO;
import fiap.restaurant.app.adapter.web.json.LoginRequestDTO;
import fiap.restaurant.app.core.domain.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginIntegrationTest extends BaseUserIntegrationTest {

    @Test
    public void login_WithValidCredentials_ReturnsSuccess() throws Exception {
        String login = "loginuser" + System.currentTimeMillis();
        String password = "password123";

        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setName("Login Test User");
        createUserDTO.setEmail("login-test@example.com");
        createUserDTO.setLogin(login);
        createUserDTO.setPassword(password);
        createUserDTO.setUserType(UserType.CUSTOMER);
        createUserDTO.setAddress(createAddressDTO());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isCreated());

        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setLogin(login);
        loginRequest.setPassword(password);

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login success"))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void login_WithInvalidPassword_ReturnsUnauthorized() throws Exception {
        String login = "wrongpass" + System.currentTimeMillis();
        String password = "correctPassword";

        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setName("Wrong Password Test");
        createUserDTO.setEmail("wrong-pass@example.com");
        createUserDTO.setLogin(login);
        createUserDTO.setPassword(password);
        createUserDTO.setUserType(UserType.CUSTOMER);
        createUserDTO.setAddress(createAddressDTO());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isCreated());

        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setLogin(login);
        loginRequest.setPassword("wrongPassword");

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid login or password"));
    }

    @Test
    public void login_WithNonExistentUser_ReturnsUnauthorized() throws Exception {
        String nonExistentLogin = "nonexistent" + System.currentTimeMillis();

        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setLogin(nonExistentLogin);
        loginRequest.setPassword("anyPassword");

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid login or password"));
    }

    @Test
    public void login_WithMissingFields_ReturnsBadRequest() throws Exception {
        LoginRequestDTO emptyRequest = new LoginRequestDTO();

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyRequest)))
                .andExpect(status().isBadRequest());

        LoginRequestDTO loginOnlyRequest = new LoginRequestDTO();
        loginOnlyRequest.setLogin("somelogin");

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginOnlyRequest)))
                .andExpect(status().isBadRequest());

        LoginRequestDTO passwordOnlyRequest = new LoginRequestDTO();
        passwordOnlyRequest.setPassword("somepassword");

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordOnlyRequest)))
                .andExpect(status().isBadRequest());
    }
} 