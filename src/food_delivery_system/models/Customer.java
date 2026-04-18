package food_delivery_system.models;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class Customer extends User {
    private List<String> addresses;

    public Customer(String firstName, String lastName, String phoneNumber, String email) {
        super(firstName, lastName, phoneNumber, email);
        addresses = new ArrayList<>();
    }

    public void addAddress(String address){
        if ( address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Adresa nu poate fi null sau gol!");
        }
        if (addresses.contains(address)) {
            throw new IllegalArgumentException("Adresa exista deja!");
        }
        addresses.add(address);
    }

    public List<String> getAddresses(){
        return Collections.unmodifiableList(addresses);
    }

    public void removeAddress(String address){
        if (!addresses.contains(address)) {
            throw new IllegalArgumentException("Adresa nu exista in lista!");
        }
        addresses.remove(address);
    }

    public String getPreferredAddress(){
        if(addresses.isEmpty())
            return null;
        return addresses.getFirst();
    }

    public void setPreferredAddress(String address){
        if(addresses.contains(address)) {
            addresses.remove(address);
            addresses.addFirst(address);
        }
        else {
            throw new IllegalArgumentException("Adresa nu există în lista clientului!");
        }
    }

    @Override
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }
}
