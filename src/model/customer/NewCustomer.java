package model.customer;
import java.util.HashMap;
import java.util.Map;

public class NewCustomer extends Customer {
    public NewCustomer(String fullname, String email, String phoneNumber) {
        super(fullname, email, phoneNumber,
              customerTypeDiscountMap.get(CustomerType.NEW));
    }
}
