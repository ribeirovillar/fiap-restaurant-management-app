package fiap.restaurant.app.core.domain;

import fiap.restaurant.app.core.exception.EmailFormatException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;
import java.util.regex.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class User {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String login;
    private String lastModifiedDate;
    private UserType userType;
    private Address address;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MIN_LOGIN_LENGTH = 3;

    public boolean isValidEmail() {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public void setEmail(String email) {
        if (email != null && !EMAIL_PATTERN.matcher(email).matches()) {
            throw new EmailFormatException("Invalid email format: " + email);
        }
        this.email = email;
    }

    public void setName(String name) {
        if (name != null && name.trim().length() < MIN_NAME_LENGTH) {
            throw new IllegalArgumentException("Name must be at least " + MIN_NAME_LENGTH + " characters long");
        }
        this.name = name;
    }

    public void setLogin(String login) {
        if (login != null && login.trim().length() < MIN_LOGIN_LENGTH) {
            throw new IllegalArgumentException("Login must be at least " + MIN_LOGIN_LENGTH + " characters long");
        }
        this.login = login;
    }

    public void setPassword(String password) {
        validatePassword(password);
        this.password = password;
    }

    public void validatePassword(String password) {
        if (password != null && password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long");
        }
    }

    public void setUserType(UserType userType) {
        if (userType == null) {
            throw new IllegalArgumentException("User type cannot be null");
        }
        this.userType = userType;
    }

    public void setAddress(Address address) {
        if (address != null) {
            address.validateForUpdate();
        }
        this.address = address;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void validateForCreation() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (!isValidEmail()) {
            throw new EmailFormatException("Invalid email format: " + email);
        }

        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException("Login is required");
        }

        if (login.trim().length() < MIN_LOGIN_LENGTH) {
            throw new IllegalArgumentException("Login must be at least " + MIN_LOGIN_LENGTH + " characters long");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        validatePassword(password);

        if (userType == null) {
            throw new IllegalArgumentException("User type is required");
        }

        if (address != null) {
            address.validateForCreation();
        }
    }

    public void validateForUpdate(User existingUser) {
        if (this.email != null && !this.email.equals(existingUser.getEmail()) && !this.isValidEmail()) {
            throw new EmailFormatException("Invalid email format: " + this.email);
        }

        if (this.name != null && this.name.trim().length() < MIN_NAME_LENGTH) {
            throw new IllegalArgumentException("Name must be at least " + MIN_NAME_LENGTH + " characters long");
        }

        if (this.login != null && this.login.trim().length() < MIN_LOGIN_LENGTH) {
            throw new IllegalArgumentException("Login must be at least " + MIN_LOGIN_LENGTH + " characters long");
        }
    }
}
