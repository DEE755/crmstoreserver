
package model.customer;

import java.util.HashMap;
import java.util.Map;

public class VIPCustomer extends Customer {
    public VIPCustomer(String fullname, String email, String phoneNumber) {
        super(fullname, email, phoneNumber,
              customerTypeDiscountMap.get(CustomerType.VIP));
    }
}
