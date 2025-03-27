package fiap.restaurant.app.adapter.web;

import fiap.restaurant.app.adapter.presenter.UserTypePresenter;
import fiap.restaurant.app.adapter.web.json.usertype.UserTypeRequestDTO;
import fiap.restaurant.app.adapter.web.json.usertype.UserTypeResponseDTO;
import fiap.restaurant.app.core.controller.UserTypeController;
import fiap.restaurant.app.core.domain.UserType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user-types")
@Tag(name = "User Types", description = "API for user type management")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserTypeApiController {

    private final UserTypeController userTypeController;
    private final UserTypePresenter userTypePresenter;

    @PostMapping
    @Operation(summary = "Create a new user type")
    public ResponseEntity<UserTypeResponseDTO> createUserType(@Valid @RequestBody UserTypeRequestDTO userTypeRequestDTO) {
        UserType userType = userTypeController.create(userTypeRequestDTO.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(userTypePresenter.mapToResponseDTO(userType));
    }

    @GetMapping
    @Operation(summary = "Get all user types")
    public ResponseEntity<List<UserTypeResponseDTO>> getAllUserTypes() {
        List<UserType> userTypes = userTypeController.findAll();
        List<UserTypeResponseDTO> responseDTOs = userTypes.stream()
                .map(userTypePresenter::mapToResponseDTO)
                .toList();
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user type by ID")
    public ResponseEntity<UserTypeResponseDTO> getUserTypeById(@PathVariable UUID id) {
        UserType userType = userTypeController.findById(id);
        return ResponseEntity.ok(userTypePresenter.mapToResponseDTO(userType));
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get user type by name")
    public ResponseEntity<UserTypeResponseDTO> getUserTypeByName(@PathVariable String name) {
        UserType userType = userTypeController.findByName(name);
        return ResponseEntity.ok(userTypePresenter.mapToResponseDTO(userType));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user type")
    public ResponseEntity<UserTypeResponseDTO> updateUserType(@PathVariable UUID id,
                                                              @Valid @RequestBody UserTypeRequestDTO userTypeRequestDTO) {
        UserType userType = userTypeController.update(id, userTypeRequestDTO.getName());
        return ResponseEntity.ok(userTypePresenter.mapToResponseDTO(userType));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user type")
    public ResponseEntity<Void> deleteUserType(@PathVariable UUID id) {
        userTypeController.delete(id);
        return ResponseEntity.noContent().build();
    }
} 