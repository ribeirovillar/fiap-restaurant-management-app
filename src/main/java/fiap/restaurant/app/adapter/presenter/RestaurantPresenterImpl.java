package fiap.restaurant.app.adapter.presenter;

import fiap.restaurant.app.adapter.database.jpa.entity.AddressEntity;
import fiap.restaurant.app.adapter.database.jpa.entity.BusinessHoursEntity;
import fiap.restaurant.app.adapter.database.jpa.entity.RestaurantEntity;
import fiap.restaurant.app.adapter.web.json.restaurant.BusinessHoursDTO;
import fiap.restaurant.app.adapter.web.json.restaurant.CreateRestaurantDTO;
import fiap.restaurant.app.adapter.web.json.restaurant.RestaurantDTO;
import fiap.restaurant.app.adapter.web.json.restaurant.UpdateRestaurantDTO;
import fiap.restaurant.app.core.domain.BusinessHours;
import fiap.restaurant.app.core.domain.Restaurant;
import fiap.restaurant.app.core.domain.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RestaurantPresenterImpl implements RestaurantPresenter {
    
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    private final AddressPresenter addressPresenter;
    private final UserPresenter userPresenter;
    private final BusinessHoursPresenter businessHoursPresenter;
    
    public RestaurantPresenterImpl(AddressPresenter addressPresenter, UserPresenter userPresenter, BusinessHoursPresenter businessHoursPresenter) {
        this.addressPresenter = addressPresenter;
        this.userPresenter = userPresenter;
        this.businessHoursPresenter = businessHoursPresenter;
    }
    
    @Override
    public Restaurant toDomain(RestaurantEntity entity) {
        if (entity == null) {
            return null;
        }
        
        List<BusinessHours> businessHours = entity.getBusinessHours().stream()
                .map(businessHoursPresenter::toDomain)
                .collect(Collectors.toList());
        
        Restaurant.RestaurantBuilder builder = Restaurant.builder()
                .id(entity.getId())
                .name(entity.getName())
                .cuisineType(entity.getCuisineType())
                .businessHours(businessHours)
                .createdAt(entity.getCreatedAt().format(FORMATTER))
                .updatedAt(entity.getUpdatedAt().format(FORMATTER));
        
        if (entity.getAddress() != null) {
            builder.address(addressPresenter.mapToDomain(entity.getAddress()));
        }
        
        if (entity.getOwner() != null) {
            builder.owner(userPresenter.mapToDomain(entity.getOwner()));
        }
        
        return builder.build();
    }
    
    @Override
    public RestaurantEntity toEntity(Restaurant domain, RestaurantEntity entity) {
        if (domain == null) {
            return null;
        }
        
        if (entity == null) {
            entity = new RestaurantEntity();
            entity.setCreatedAt(LocalDateTime.now());
        }
        
        entity.setName(domain.getName());
        entity.setCuisineType(domain.getCuisineType());
        entity.setUpdatedAt(LocalDateTime.now());
        
        if (domain.getOwner() != null) {
            entity.setOwner(userPresenter.mapToEntity(domain.getOwner()));
        }
        
        if (domain.getAddress() != null) {
            AddressEntity addressEntity = new AddressEntity();
            addressEntity.setId(domain.getAddress().getId());
            addressEntity.setStreet(domain.getAddress().getStreet());
            addressEntity.setCity(domain.getAddress().getCity());
            addressEntity.setState(domain.getAddress().getState());
            addressEntity.setZipCode(domain.getAddress().getZipCode());
            addressEntity.setCountry(domain.getAddress().getCountry());
            entity.setAddress(addressEntity);
        }
        
        return entity;
    }
    
    @Override
    public void updateBusinessHours(RestaurantEntity entity, List<BusinessHours> businessHours) {
        entity.getBusinessHours().clear();
        
        if (businessHours != null) {
            businessHours.forEach(hours -> {
                BusinessHoursEntity hoursEntity = businessHoursPresenter.toEntity(hours, null);
                hoursEntity.setRestaurant(entity);
                entity.getBusinessHours().add(hoursEntity);
            });
        }
    }

    @Override
    public Restaurant toCreateDomain(CreateRestaurantDTO dto, User owner) {
        if (dto == null) {
            return null;
        }

        Restaurant.RestaurantBuilder builder = Restaurant.builder()
                .name(dto.getName())
                .cuisineType(dto.getCuisineType())
                .businessHours(mapBusinessHoursToDomain(dto.getBusinessHours()))
                .owner(owner);
                
        if (dto.getAddress() != null) {
            builder.address(addressPresenter.mapToDomain(dto.getAddress()));
        }
        
        return builder.build();
    }

    @Override
    public Restaurant toUpdateDomain(UpdateRestaurantDTO dto, Restaurant existingRestaurant) {
        if (dto == null) {
            return null;
        }

        Restaurant.RestaurantBuilder builder = Restaurant.builder()
                .name(dto.getName())
                .cuisineType(dto.getCuisineType())
                .businessHours(mapBusinessHoursToDomain(dto.getBusinessHours()));
                
        if (dto.getAddress() != null) {
            builder.address(addressPresenter.mapToDomain(dto.getAddress()));
        }

        if (existingRestaurant != null) {
            builder.id(existingRestaurant.getId())
                  .owner(existingRestaurant.getOwner())
                  .createdAt(existingRestaurant.getCreatedAt())
                  .updatedAt(existingRestaurant.getUpdatedAt());
        }

        return builder.build();
    }

    @Override
    public RestaurantDTO toDTO(Restaurant domain) {
        if (domain == null) {
            return null;
        }

        RestaurantDTO.RestaurantDTOBuilder builder = RestaurantDTO.builder()
                .id(domain.getId())
                .name(domain.getName())
                .cuisineType(domain.getCuisineType())
                .businessHours(mapBusinessHoursToDTO(domain.getBusinessHours()))
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt());

        if (domain.getOwner() != null) {
            builder.ownerId(domain.getOwner().getId());
            builder.ownerName(domain.getOwner().getName());
        }
        
        if (domain.getAddress() != null) {
            builder.address(addressPresenter.mapToDTO(domain.getAddress()));
        }

        return builder.build();
    }
    
    @Override
    public List<RestaurantDTO> toDTOList(List<Restaurant> restaurants) {
        if (restaurants == null) {
            return List.of();
        }
        
        return restaurants.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private List<BusinessHours> mapBusinessHoursToDomain(List<BusinessHoursDTO> dtos) {
        if (dtos == null) {
            return List.of();
        }

        return dtos.stream()
                .map(dto -> BusinessHours.builder()
                        .dayOfWeek(dto.getDayOfWeek())
                        .openingTime(dto.getOpeningTime())
                        .closingTime(dto.getClosingTime())
                        .isClosed(dto.isClosed())
                        .build())
                .collect(Collectors.toList());
    }

    private List<BusinessHoursDTO> mapBusinessHoursToDTO(List<BusinessHours> domains) {
        if (domains == null) {
            return List.of();
        }

        return domains.stream()
                .map(domain -> BusinessHoursDTO.builder()
                        .dayOfWeek(domain.getDayOfWeek())
                        .openingTime(domain.getOpeningTime())
                        .closingTime(domain.getClosingTime())
                        .isClosed(domain.isClosed())
                        .build())
                .collect(Collectors.toList());
    }
} 