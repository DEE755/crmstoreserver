package utils;

import java.io.IOException;
import java.util.List;
import model.customer.Customer;
import serialization.CustomerSerializer;
import serialization.EmployeeSerializer;



public class Utility {

private static CustomerSerializer customerSerializer = new CustomerSerializer();
private static EmployeeSerializer employeeSerializer = new EmployeeSerializer();

    public static int calculateLastId() {

        try {
            List<Customer> customers = customerSerializer.loadCustomerList("customers.ser");
            if (!customers.isEmpty()) {
                return customers.get(customers.size() - 1).getId() + 1;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("No customer data found. Starting IDs from 1.");
        }

        return 1; 
    }
}
