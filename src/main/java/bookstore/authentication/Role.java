package bookstore.authentication;

public enum Role {
    USER("USER"), ADMIN("ADMIN");

    public final String role;

    private Role(String role) {
        this.role = role;
    }
}
