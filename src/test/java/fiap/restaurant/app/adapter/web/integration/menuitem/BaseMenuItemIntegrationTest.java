package fiap.restaurant.app.adapter.web.integration.menuitem;

import com.fasterxml.jackson.databind.ObjectMapper;
import fiap.restaurant.app.adapter.web.json.common.AddressDTO;
import fiap.restaurant.app.adapter.web.json.menuitem.UpsertMenuItemDTO;
import fiap.restaurant.app.adapter.web.json.restaurant.BusinessHoursDTO;
import fiap.restaurant.app.adapter.web.json.restaurant.CreateRestaurantDTO;
import fiap.restaurant.app.adapter.web.json.restaurant.RestaurantDTO;
import fiap.restaurant.app.adapter.web.json.user.CreateUserDTO;
import fiap.restaurant.app.adapter.web.json.user.UserResponseDTO;
import fiap.restaurant.app.configuration.TestSecurityConfig;
import fiap.restaurant.app.core.domain.CuisineType;
import fiap.restaurant.app.util.UserTypeTestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@Transactional
public abstract class BaseMenuItemIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
    
    protected UUID createTestOwner() throws Exception {
        CreateUserDTO ownerDTO = new CreateUserDTO();
        ownerDTO.setName("Test Restaurant Owner");
        ownerDTO.setEmail("restaurant.owner" + System.currentTimeMillis() + "@example.com");
        ownerDTO.setLogin("restaurantowner" + System.currentTimeMillis());
        ownerDTO.setPassword("password123");
        ownerDTO.setUserType(UserTypeTestHelper.createOwnerDTO());
        
        AddressDTO userAddressDTO = new AddressDTO();
        userAddressDTO.setStreet("Test Street");
        userAddressDTO.setCity("Test City");
        userAddressDTO.setState("TS");
        userAddressDTO.setZipCode("12345");
        userAddressDTO.setCountry("Test Country");
        ownerDTO.setAddress(userAddressDTO);
        
        MvcResult result = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ownerDTO)))
                .andExpect(status().isCreated())
                .andReturn();
        
        UserResponseDTO createdOwner = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UserResponseDTO.class
        );
        
        return createdOwner.getId();
    }
    
    protected UUID createTestRestaurant(UUID ownerId) throws Exception {
        CreateRestaurantDTO restaurantDTO = new CreateRestaurantDTO();
        restaurantDTO.setName("Test Restaurant");
        restaurantDTO.setCuisineType(CuisineType.ITALIAN);
        restaurantDTO.setOwnerId(ownerId);
        restaurantDTO.setAddress(createAddressDTO());
        restaurantDTO.setBusinessHours(createBusinessHours());
        
        MvcResult result = mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restaurantDTO)))
                .andExpect(status().isCreated())
                .andReturn();
        
        RestaurantDTO createdRestaurant = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                RestaurantDTO.class
        );
        
        return createdRestaurant.getId();
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
    
    protected List<BusinessHoursDTO> createBusinessHours() {
        List<BusinessHoursDTO> businessHours = new ArrayList<>();
        
        businessHours.add(createBusinessHourDTO(DayOfWeek.MONDAY, "09:00", "18:00", false));
        businessHours.add(createBusinessHourDTO(DayOfWeek.TUESDAY, "09:00", "18:00", false));
        
        return businessHours;
    }
    
    protected BusinessHoursDTO createBusinessHourDTO(DayOfWeek dayOfWeek, String opening, String closing, boolean isClosed) {
        BusinessHoursDTO businessHoursDTO = new BusinessHoursDTO();
        businessHoursDTO.setDayOfWeek(dayOfWeek);
        
        if (!isClosed) {
            businessHoursDTO.setOpeningTime(LocalTime.parse(opening));
            businessHoursDTO.setClosingTime(LocalTime.parse(closing));
        }
        
        businessHoursDTO.setClosed(isClosed);
        return businessHoursDTO;
    }
    
    protected UpsertMenuItemDTO createMenuItemDTO() {
        UpsertMenuItemDTO menuItemDTO = new UpsertMenuItemDTO();
        menuItemDTO.setName("Test Menu Item");
        menuItemDTO.setDescription("This is a description for the test menu item that is sufficiently long");
        menuItemDTO.setPrice(new BigDecimal("15.99"));
        menuItemDTO.setAvailableForTakeout(true);
        menuItemDTO.setPhotoPath("/images/test.jpg");
        return menuItemDTO;
    }
} 