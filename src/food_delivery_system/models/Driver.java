package food_delivery_system.models;

public class Driver extends User{
    private String vehicleType;
    private boolean isAvailable;

    public Driver(String firstName, String lastName, String phoneNumber, String email) {
        super(firstName, lastName, phoneNumber, email);
        vehicleType = "";
        isAvailable = false;
    }

    @Override
    public String getFullName() {
        return "Driver: " + getFirstName();
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
