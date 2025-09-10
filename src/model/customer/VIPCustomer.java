
package model.customer;

public class VIPCustomer extends Customer {
    public VIPCustomer(String firstName, String familyName, String email, String phoneNumber) {
        super(firstName, familyName, email, phoneNumber,
              customerTypeDiscountMap.get(CustomerType.VIP));
    }


    public VIPCustomer(int givenId,String firstName, String familyName, String email, String phoneNumber, double discount) {
        super(givenId, firstName, familyName, email, phoneNumber, discount);
    }
}
