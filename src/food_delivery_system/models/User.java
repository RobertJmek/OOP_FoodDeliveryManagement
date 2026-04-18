package food_delivery_system.models;

public abstract class User {
    private static int idGenerator = 0;
    private final Integer id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

    private User() {
        id = ++idGenerator;
    }

    protected User(String firstName, String lastName, String phoneNumber, String email) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        setPhoneNumber(phoneNumber);
        setEmail(email);
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public abstract String getFullName();

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {

        if (phoneNumber == null || !phoneNumber.matches("^\\d{10}$")) {
            throw new IllegalArgumentException("Invalid phone number! It must contain exactly 10 digits.");
        }
        this.phoneNumber = phoneNumber;
    }
    public void setEmail(String email) {
        // Validates that the email is not null and follows a standard format (e.g., name@domain.com)
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format!");
        }
        this.email = email;
    }
}
