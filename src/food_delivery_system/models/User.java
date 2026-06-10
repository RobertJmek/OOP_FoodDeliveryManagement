package food_delivery_system.models;

public abstract class User {
    private int id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

    protected User(String firstName, String lastName, String phoneNumber, String email) {
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
        setEmail(email);
    }

    protected User(int id, String firstName, String lastName, String phoneNumber, String email) {
        this.id = id;
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
        setEmail(email);
    }

    public void setId(int id) {
        this.id = id;
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

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty!");
        }
        this.firstName = firstName.trim();
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty!");
        }
        this.lastName = lastName.trim();
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

    public String getFullName() {
        return String.format("%s %s", getFirstName(), getLastName());
    }
}
