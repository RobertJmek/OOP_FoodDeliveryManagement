package food_delivery_system.models;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class Customer extends User {
    private List<String> addresses;

    // Used when creating a new customer (DB will assign ID)
    public Customer(String firstName, String lastName, String phoneNumber, String email) {
        super(firstName, lastName, phoneNumber, email);
        addresses = new ArrayList<>();
    }

    // Used when reconstructing a customer from the DB
    public Customer(int id, String firstName, String lastName, String phoneNumber, String email) {
        super(id, firstName, lastName, phoneNumber, email);
        addresses = new ArrayList<>();
    }

    public void addAddress(String address){
        if (address == null || address.isEmpty()) {
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
        return addresses.get(0);        // Java 17 compatible (getFirst() is Java 21+)
    }

    public void setPreferredAddress(String address){
        if(addresses.contains(address)) {
            addresses.remove(address);
            addresses.add(0, address);  // Java 17 compatible (addFirst() is Java 21+)
        } else {
            throw new IllegalArgumentException("Adresa nu există în lista clientului!");
        }
    }
}
