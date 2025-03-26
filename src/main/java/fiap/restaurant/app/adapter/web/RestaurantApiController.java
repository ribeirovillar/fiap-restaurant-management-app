package fiap.restaurant.app.adapter.web;

import fiap.restaurant.app.adapter.presenter.RestaurantPresenter;
import fiap.restaurant.app.adapter.web.json.restaurant.CreateRestaurantDTO;
import fiap.restaurant.app.adapter.web.json.restaurant.RestaurantDTO;
import fiap.restaurant.app.adapter.web.json.restaurant.UpdateRestaurantDTO;
import fiap.restaurant.app.core.controller.RestaurantController;
import fiap.restaurant.app.core.domain.Restaurant;
import fiap.restaurant.app.core.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/restaurants")
@Tag(name = "Restaurants", description = "API for restaurant management")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class RestaurantApiController {

    private final RestaurantController restaurantController;
    private final RestaurantPresenter restaurantPresenter;

    @PostMapping
    @Operation(summary = "Create a new restaurant")
    public ResponseEntity<RestaurantDTO> createRestaurant(@RequestBody @Valid CreateRestaurantDTO createRestaurantDTO) {
        User owner = restaurantController.findUserById(createRestaurantDTO.getOwnerId());
        Restaurant restaurant = restaurantPresenter.toCreateDomain(createRestaurantDTO, owner);
        Restaurant createdRestaurant = restaurantController.create(restaurant);
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantPresenter.toDTO(createdRestaurant));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get restaurant by ID")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable UUID id) {
        Restaurant restaurant = restaurantController.findById(id);
        return ResponseEntity.ok(restaurantPresenter.toDTO(restaurant));
    }

    @GetMapping
    @Operation(summary = "Get all restaurants")
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantController.findAll();
        return ResponseEntity.ok(restaurantPresenter.toDTOList(restaurants));
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Get restaurants by owner ID")
    public ResponseEntity<List<RestaurantDTO>> getRestaurantsByOwnerId(@PathVariable UUID ownerId) {
        List<Restaurant> restaurants = restaurantController.findByOwnerId(ownerId);
        return ResponseEntity.ok(restaurantPresenter.toDTOList(restaurants));
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Find restaurants by name")
    public ResponseEntity<List<RestaurantDTO>> findRestaurantsByName(@PathVariable String name) {
        List<Restaurant> restaurants = restaurantController.findByName(name);
        return ResponseEntity.ok(restaurantPresenter.toDTOList(restaurants));
    }

    @GetMapping("/cuisine/{cuisineType}")
    @Operation(summary = "Find restaurants by cuisine type")
    public ResponseEntity<List<RestaurantDTO>> findRestaurantsByCuisineType(@PathVariable String cuisineType) {
        List<Restaurant> restaurants = restaurantController.findByCuisineType(cuisineType);
        return ResponseEntity.ok(restaurantPresenter.toDTOList(restaurants));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing restaurant")
    public ResponseEntity<RestaurantDTO> updateRestaurant(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateRestaurantDTO updateRestaurantDTO) {
        Restaurant restaurant = restaurantPresenter.toUpdateDomain(updateRestaurantDTO, null);
        Restaurant updatedRestaurant = restaurantController.update(id, restaurant);
        return ResponseEntity.ok(restaurantPresenter.toDTO(updatedRestaurant));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a restaurant by ID")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable UUID id) {
        restaurantController.delete(id);
        return ResponseEntity.noContent().build();
    }
} 