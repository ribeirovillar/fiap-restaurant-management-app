package fiap.restaurant.app.core.domain;

import fiap.restaurant.app.core.exception.BusinessException;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserType {
    private UUID id;
    private String name;
    
    public static final String CUSTOMER = "CUSTOMER";
    public static final String OWNER = "OWNER";

    public UserType(UUID id, String name) {
        validateName(name);
        this.id = id;
        this.name = name;
    }

    public UserType(String name) {
        this(UUID.randomUUID(), name);
    }
    
    public UserType() {
        // Required for JPA
        this.id = UUID.randomUUID();
    }
    
    public static UserTypeBuilder builder() {
        return new UserTypeBuilder();
    }

    public void update(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException("User type name cannot be empty");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserType userType = (UserType) o;

        return id != null ? id.equals(userType.id) : userType.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    public static class UserTypeBuilder {
        private UUID id;
        private String name;
        
        UserTypeBuilder() {}
        
        public UserTypeBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        
        public UserTypeBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        public UserType build() {
            // Don't generate UUID automatically if not provided
            return new UserType(id, name);
        }
    }
}
