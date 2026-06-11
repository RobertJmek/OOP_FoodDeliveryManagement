package food_delivery_system.models;

public class Manager extends User{

    public Manager(String firstName, String lastName, String phoneNumber, String email) {
        super(firstName, lastName, phoneNumber, email);
    }

    public Manager(int id, String firstName, String lastName, String phoneNumber, String email) {
        super(id, firstName, lastName, phoneNumber, email);
    }
}
