package food_delivery_system.models;

public class Manager extends User{

    public Manager(String firstName, String lastName, String phoneNumber, String email) {
        super(firstName, lastName, phoneNumber, email);
    }

    @Override
    public String getFullName() {
        return "Manager: " + getFirstName() + " " + getLastName();
    }
}
