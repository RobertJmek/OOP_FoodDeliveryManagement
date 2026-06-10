package food_delivery_system.models;

public class Driver extends User{
    private String vehicleType;
    private boolean isAvailable;

    public Driver(String firstName, String lastName, String phoneNumber, String email) {
        super(firstName, lastName, phoneNumber, email);
        vehicleType = "";
        isAvailable = false;
    }

    public Driver(int id,String firstName, String lastName, String phoneNumber, String email) {
        super(id, firstName, lastName, phoneNumber, email);
        vehicleType = "";
        isAvailable = false;
    }

    public String getFullName() {
        return "Driver: " + getFirstName() + " " + getLastName();
    }

    public String getVehicleType() {
        return vehicleType;
    }
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
