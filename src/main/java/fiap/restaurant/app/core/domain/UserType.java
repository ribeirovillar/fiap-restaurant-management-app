package fiap.restaurant.app.core.domain;

public enum UserType {
    CUSTOMER,
    OWNER;

    public String getName() {
        return this.name();
    }
}
