package util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import model.Employee;
import model.customer.Customer;
import serialization.CustomerSerializer;
import serialization.EmployeeSerializer;



public class Utility {

private static CustomerSerializer customerSerializer = new CustomerSerializer();
private static EmployeeSerializer employeeSerializer = new EmployeeSerializer();

    public static int calculateLastId() {

        try {
            List<Customer> customers = customerSerializer.loadCustomerList();
            if (!customers.isEmpty()) {
                return customers.get(customers.size() - 1).getId() + 1;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("No customer data found. Starting IDs from 1.");
        }

        return 1; 
    }


     public static void createFileIfNotExists() {
        File employeeFile = new File(Constants.EMPLOYEE_FILE);
        if (!employeeFile.exists()) {
            try {
                employeeSerializer.saveEmployee(
                    new Employee(0, "administrator", "admin@admin.com", "admin", "admin", "00000000")
                );

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error saving employee: " + e.getMessage());
            }
        }
    }
}
