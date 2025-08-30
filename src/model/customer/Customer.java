package model.customer;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

import util.Utility;

public abstract class Customer implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    private String fullname;
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

    public String getEmail() {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public enum CustomerType {
        NEW, RETURNING, VIP
    }

    public Customer(String fullname, String email, String phoneNumber, double discount) {
        this.fullname = fullname;
        this.email = email;
        this.id = Utility.calculateLastId();
        this.phoneNumber = phoneNumber;
        this.discount = discount;
    }

    public double calculatePrice(double basePrice) {
        return basePrice * (1 - discount);
    }

    public double getDiscount() {
        return discount;
    }

    public int getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }
}
