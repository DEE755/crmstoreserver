package model.customer;

public class NewCustomer extends Customer {
    public NewCustomer(String fullname, String email, String phoneNumber) {
        super(fullname, email, phoneNumber,
              customerTypeDiscountMap.get(CustomerType.NEW));
    }

    public NewCustomer(int givenId, String fullname, String email, String phoneNumber, double discount) {
        super(givenId, fullname, email, phoneNumber, discount);
    }
}

