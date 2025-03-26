package fiap.restaurant.app.configuration;

import fiap.restaurant.app.core.controller.RestaurantController;
import fiap.restaurant.app.core.gateway.RestaurantGateway;
import fiap.restaurant.app.core.gateway.UserGateway;
import fiap.restaurant.app.core.usecase.restaurant.*;
import fiap.restaurant.app.core.usecase.user.FindUserByIdUseCase;
import fiap.restaurant.app.core.usecase.user.FindUserByLoginUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantBeanConfiguration {

    @Bean
    public CreateRestaurantUseCase createRestaurantUseCase(RestaurantGateway restaurantGateway) {
        return new CreateRestaurantUseCaseImpl(restaurantGateway);
    }

    @Bean
    public FindRestaurantByIdUseCase findRestaurantByIdUseCase(RestaurantGateway restaurantGateway) {
        return new FindRestaurantByIdUseCaseImpl(restaurantGateway);
    }

    @Bean
    public FindAllRestaurantsUseCase findAllRestaurantsUseCase(RestaurantGateway restaurantGateway) {
        return new FindAllRestaurantsUseCaseImpl(restaurantGateway);
    }

    @Bean
    public FindRestaurantsByOwnerIdUseCase findRestaurantsByOwnerIdUseCase(RestaurantGateway restaurantGateway) {
        return new FindRestaurantsByOwnerIdUseCaseImpl(restaurantGateway);
    }

    @Bean
    public FindRestaurantsByNameUseCase findRestaurantsByNameUseCase(RestaurantGateway restaurantGateway) {
        return new FindRestaurantsByNameUseCaseImpl(restaurantGateway);
    }

    @Bean
    public FindRestaurantsByCuisineTypeUseCase findRestaurantsByCuisineTypeUseCase(RestaurantGateway restaurantGateway) {
        return new FindRestaurantsByCuisineTypeUseCaseImpl(restaurantGateway);
    }

    @Bean
    public UpdateRestaurantUseCase updateRestaurantUseCase(RestaurantGateway restaurantGateway) {
        return new UpdateRestaurantUseCaseImpl(restaurantGateway, findRestaurantByIdUseCase(restaurantGateway));
    }

    @Bean
    public DeleteRestaurantUseCase deleteRestaurantUseCase(RestaurantGateway restaurantGateway) {
        return new DeleteRestaurantUseCaseImpl(restaurantGateway, findRestaurantByIdUseCase(restaurantGateway));
    }

    @Bean
    public FindUserByLoginUseCase findUserByLoginUseCase(UserGateway userGateway) {
        return new FindUserByLoginUseCase(userGateway);
    }

    @Bean
    public RestaurantController restaurantController(
            CreateRestaurantUseCase createRestaurantUseCase,
            FindRestaurantByIdUseCase findRestaurantByIdUseCase,
            FindAllRestaurantsUseCase findAllRestaurantsUseCase,
            FindRestaurantsByOwnerIdUseCase findRestaurantsByOwnerIdUseCase,
            FindRestaurantsByNameUseCase findRestaurantsByNameUseCase,
            FindRestaurantsByCuisineTypeUseCase findRestaurantsByCuisineTypeUseCase,
            UpdateRestaurantUseCase updateRestaurantUseCase,
            DeleteRestaurantUseCase deleteRestaurantUseCase,
            FindUserByIdUseCase findUserByIdUseCase) {

        return new RestaurantController(
                createRestaurantUseCase,
                findRestaurantByIdUseCase,
                findAllRestaurantsUseCase,
                findRestaurantsByOwnerIdUseCase,
                findRestaurantsByNameUseCase,
                findRestaurantsByCuisineTypeUseCase,
                updateRestaurantUseCase,
                deleteRestaurantUseCase,
                findUserByIdUseCase);
    }
} 