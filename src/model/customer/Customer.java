package model.customer;

import java.io.Serial;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.Utility;

public abstract class Customer implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    private String firstName;
    private String familyName;
    private String email;
    private int id;
    private String phoneNumber;
    private double discount;

    private static int idCounter = 0;

    protected static final Map<CustomerType, Double> customerTypeDiscountMap = new HashMap<>();

    static {
        customerTypeDiscountMap.put(CustomerType.NEW, 0.1);
        customerTypeDiscountMap.put(CustomerType.RETURNING, 0.05);
        customerTypeDiscountMap.put(CustomerType.VIP, 0.2);
    }


    public enum CustomerType {
        NEW, RETURNING, VIP
    }

    //those constuctors can be only used as super from subclasses (abstract class)
    //creating a totally new customer
    public Customer(String firstName, String familyName, String email, String phoneNumber, double discount) {
        this.firstName = firstName;
        this.familyName = familyName;
        this.email = email;
        this.id = Utility.calculateLastId();
        this.phoneNumber = phoneNumber;
        this.discount = discount;
    }

    //creating a customer from already existing 
     public Customer(int givenId, String firstName, String familyName, String email, String phoneNumber, double discount) {
        this.firstName = firstName;
        this.familyName = familyName;
        this.email = email;
        this.id = givenId;
        this.phoneNumber = phoneNumber;
        this.discount = discount;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }


    public double getDiscount() {
        return discount;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getFullName() {
        return firstName + " " + familyName;
    }

    public double calculatePrice(double basePrice) {
        return basePrice * (1 - discount);
    }

    public static int findCustomerIndexById(List<Customer> customers, int searchId) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getId() == searchId) {
                return i;
            }
        }
        return -1;
    }

    
}
