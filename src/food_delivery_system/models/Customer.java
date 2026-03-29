package food_delivery_system.models;
import java.util.List;
import java.util.ArrayList;

public class Customer extends User {
    private List<String> addresses;

    public Customer(String firstName, String lastName, String phoneNumber, String email) {
        super(firstName, lastName, phoneNumber, email);
        addresses = new ArrayList<>();
    }

    public void addAddress(String address){
        addresses.add(address);
    }

    public List<String> getAddresses(){
        return addresses;
    }

    public void removeAddress(String address){
        addresses.remove(address);
    }

    public String getPrefferedAddress(){
        if(addresses.isEmpty())
            return null;
        return addresses.getFirst();
    }

    public void setPrefferedAddress(String address){
        if(addresses.contains(address)) {
            addresses.remove(address);
            addresses.addFirst(address);
        }

    }

    @Override
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }
}
