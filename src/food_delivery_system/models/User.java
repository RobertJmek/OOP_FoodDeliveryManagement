package food_delivery_system.models;

public abstract class User {
    static int idGenerator = 0;
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
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Integer getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }

    public abstract String getFullName();

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmail(String email) { this.email = email; }

}
