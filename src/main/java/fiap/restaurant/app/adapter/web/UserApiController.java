package fiap.restaurant.app.adapter.web;

import fiap.restaurant.app.adapter.presenter.UserPresenter;
import fiap.restaurant.app.adapter.web.json.*;
import fiap.restaurant.app.core.controller.UserController;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "API for user management")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserApiController {

    UserController userController;
    UserPresenter userPresenter;

    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody CreateUserDTO request) {
        return new ResponseEntity<>(userPresenter.mapToResponseDTO(userController.create(userPresenter.mapToDomain(request))), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return new ResponseEntity<>(
                userController.findAll()
                        .stream()
                        .map(userPresenter::mapToResponseDTO)
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userPresenter.mapToResponseDTO(userController.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id, @Valid @RequestBody UpdateUserDTO request) {
        return ResponseEntity.ok(userPresenter.mapToResponseDTO(userController.update(id, userPresenter.mapToDomain(request))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user by ID")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userController.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/login")
    @Operation(summary = "Validate user login credentials")
    public ResponseEntity<LoginResponseDTO> validateLogin(@Valid @RequestBody LoginRequestDTO request) {
        userController.validateLogin(request.getLogin(), request.getPassword());
        return new ResponseEntity<>(
                new LoginResponseDTO("Login success", true),
                HttpStatus.OK
        );
    }

    @PostMapping("/password")
    @Operation(summary = "Update user password after validating current credentials")
    public ResponseEntity<PasswordResponseDTO> updatePassword(@Valid @RequestBody UpdatePasswordDTO request) {
        userController.updatePassword(
                request.getLogin(),
                request.getCurrentPassword(),
                request.getNewPassword()
        );

        return new ResponseEntity<>(
                new PasswordResponseDTO("Password updated successfully", true),
                HttpStatus.OK
        );
    }
}
