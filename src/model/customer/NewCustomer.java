package model.customer;

public class NewCustomer extends Customer {
    public NewCustomer(String firstName, String familyName, String email, String phoneNumber) {
        super(firstName, familyName, email, phoneNumber,
              customerTypeDiscountMap.get(CustomerType.NEW));
    }

    public NewCustomer(int givenId, String firstName, String familyName, String email, String phoneNumber, double discount) {
        super(givenId, firstName, familyName, email, phoneNumber, discount);
        
    }
}
