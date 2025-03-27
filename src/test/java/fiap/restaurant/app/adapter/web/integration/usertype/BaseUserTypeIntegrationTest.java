package fiap.restaurant.app.adapter.web.integration.usertype;

import com.fasterxml.jackson.databind.ObjectMapper;
import fiap.restaurant.app.adapter.web.json.usertype.UserTypeRequestDTO;
import fiap.restaurant.app.adapter.web.json.usertype.UserTypeResponseDTO;
import fiap.restaurant.app.configuration.TestSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@Transactional
public abstract class BaseUserTypeIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected UserTypeResponseDTO createUserTypeAndReturn(String typeName) throws Exception {
        UserTypeRequestDTO userTypeRequestDTO = new UserTypeRequestDTO();
        userTypeRequestDTO.setName(typeName);

        MvcResult result = mockMvc.perform(post("/api/v1/user-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userTypeRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readValue(result.getResponse().getContentAsString(), UserTypeResponseDTO.class);
    }

    protected String generateUniqueName(String prefix) {
        return prefix + System.currentTimeMillis();
    }
} 