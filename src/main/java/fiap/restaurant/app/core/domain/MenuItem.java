package fiap.restaurant.app.core.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class MenuItem {
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MIN_DESCRIPTION_LENGTH = 10;
    private static final int MAX_DESCRIPTION_LENGTH = 500;
    private static final BigDecimal MIN_PRICE = BigDecimal.valueOf(0.01);

    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean availableForTakeout;
    private String photoPath;
    private Restaurant restaurant;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("Name must be between " + MIN_NAME_LENGTH + " and " + MAX_NAME_LENGTH + " characters");
        }
        this.name = name.trim();
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }
        if (description.length() < MIN_DESCRIPTION_LENGTH || description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("Description must be between " + MIN_DESCRIPTION_LENGTH + " and " + MAX_DESCRIPTION_LENGTH + " characters");
        }
        this.description = description.trim();
    }

    public void setPrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("Price is required");
        }
        if (price.compareTo(MIN_PRICE) < 0) {
            throw new IllegalArgumentException("Price must be greater than " + MIN_PRICE);
        }
        this.price = price;
    }

    public void setPhotoPath(String photoPath) {
        if (photoPath == null || photoPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Photo path is required");
        }
        this.photoPath = photoPath.trim();
    }

    public void setRestaurant(Restaurant restaurant) {
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant is required");
        }
        this.restaurant = restaurant;
    }

    public void validateForCreation() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }
        if (price == null) {
            throw new IllegalArgumentException("Price is required");
        }
        if (photoPath == null || photoPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Photo path is required");
        }
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant is required");
        }
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("Name must be between " + MIN_NAME_LENGTH + " and " + MAX_NAME_LENGTH + " characters");
        }
        if (description.length() < MIN_DESCRIPTION_LENGTH || description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("Description must be between " + MIN_DESCRIPTION_LENGTH + " and " + MAX_DESCRIPTION_LENGTH + " characters");
        }
        if (price.compareTo(MIN_PRICE) < 0) {
            throw new IllegalArgumentException("Price must be greater than " + MIN_PRICE);
        }
    }

    public void validateForUpdate() {
        validateForCreation();
    }

    public void validateForDelete() {
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant is required");
        }
    }
} 