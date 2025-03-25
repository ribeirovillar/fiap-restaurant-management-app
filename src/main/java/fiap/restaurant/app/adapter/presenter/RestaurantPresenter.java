package fiap.restaurant.app.adapter.presenter;

import fiap.restaurant.app.adapter.database.jpa.entity.RestaurantEntity;
import fiap.restaurant.app.adapter.web.json.restaurant.CreateRestaurantDTO;
import fiap.restaurant.app.adapter.web.json.restaurant.RestaurantDTO;
import fiap.restaurant.app.adapter.web.json.restaurant.UpdateRestaurantDTO;
import fiap.restaurant.app.core.domain.BusinessHours;
import fiap.restaurant.app.core.domain.Restaurant;
import fiap.restaurant.app.core.domain.User;

import java.util.List;

public interface RestaurantPresenter {
    
    Restaurant toDomain(RestaurantEntity entity);
    
    RestaurantEntity toEntity(Restaurant domain, RestaurantEntity entity);
    
    void updateBusinessHours(RestaurantEntity entity, List<BusinessHours> businessHours);

    Restaurant toCreateDomain(CreateRestaurantDTO dto, User owner);

    Restaurant toUpdateDomain(UpdateRestaurantDTO dto, Restaurant existingRestaurant);

    RestaurantDTO toDTO(Restaurant domain);
    
    List<RestaurantDTO> toDTOList(List<Restaurant> restaurants);
} 