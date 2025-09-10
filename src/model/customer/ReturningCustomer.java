package model.customer;

public class ReturningCustomer extends Customer {
    public ReturningCustomer(String fullname, String email, String phoneNumber) {
        super(fullname, email, phoneNumber,
              customerTypeDiscountMap.get(CustomerType.RETURNING));
    }

    public ReturningCustomer(int givenId, String fullname, String email, String phoneNumber, double discount) {
        super(givenId, fullname, email, phoneNumber, discount);
    }
}
