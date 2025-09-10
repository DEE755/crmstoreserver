
package model.customer;

public class VIPCustomer extends Customer {
    public VIPCustomer(String fullname, String email, String phoneNumber) {
        super(fullname, email, phoneNumber,
              customerTypeDiscountMap.get(CustomerType.VIP));
    }


    public VIPCustomer(int givenId,String fullname, String email, String phoneNumber, double discount) {
        super(givenId, fullname, email, phoneNumber, discount);
    }
}
