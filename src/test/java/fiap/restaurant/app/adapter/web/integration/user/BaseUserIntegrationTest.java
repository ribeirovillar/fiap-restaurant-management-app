package fiap.restaurant.app.adapter.web.integration.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import fiap.restaurant.app.adapter.web.json.common.AddressDTO;
import fiap.restaurant.app.adapter.web.json.user.CreateUserDTO;
import fiap.restaurant.app.configuration.TestSecurityConfig;
import fiap.restaurant.app.core.domain.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@Transactional
public abstract class BaseUserIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected CreateUserDTO createUserDTO(String name, String email, String login) {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName(name);
        userDTO.setEmail(email);
        userDTO.setLogin(login + System.currentTimeMillis());
        userDTO.setPassword("password123");
        userDTO.setUserType(UserType.CUSTOMER);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet("Test Street");
        addressDTO.setCity("Test City");
        addressDTO.setState("TS");
        addressDTO.setZipCode("12345");
        addressDTO.setCountry("Test Country");
        userDTO.setAddress(addressDTO);

        return userDTO;
    }

    protected AddressDTO createAddressDTO() {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet("Test Street");
        addressDTO.setCity("Test City");
        addressDTO.setState("TS");
        addressDTO.setZipCode("12345");
        addressDTO.setCountry("Test Country");
        return addressDTO;
    }
} 