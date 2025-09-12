package util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import main.Servers;
import model.Employee;
import model.Employee.Role;
import model.customer.Customer;
import serialization.CustomerSerializer;
import serialization.EmployeeSerializer;



public class Utility {

private static CustomerSerializer customerSerializer = CustomerSerializer.getInstance();
private static EmployeeSerializer employeeSerializer = EmployeeSerializer.getInstance();

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


     public static void createEmployeeFileIfNotExists() {
        File employeeFile = new File(Servers.currentBranch.get().getEmployeeFilePath());
        if (!employeeFile.exists()) {
            try {
                employeeSerializer.saveEmployee( new Employee(0, "administrator", "administrator", "admin@admin.com", "admin", "admin", "00000000", Role.ADMIN, Servers.currentBranch.get())
                );

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error saving employee: " + e.getMessage());
            }
        }
    }
}
