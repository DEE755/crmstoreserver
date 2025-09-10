package model.customer;

public class ReturningCustomer extends Customer {
    public ReturningCustomer(String firstName, String familyName, String email, String phoneNumber) {
        super(firstName, familyName, email, phoneNumber,
              customerTypeDiscountMap.get(CustomerType.RETURNING));
    }

    public ReturningCustomer(int givenId, String firstName, String familyName, String email, String phoneNumber, double discount) {
        super(givenId, firstName, familyName, email, phoneNumber, discount);
    }
}
