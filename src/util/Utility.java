package util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import main.Servers;
import model.Branch;
import model.Employee;
import model.Employee.Role;
import model.customer.Customer;
import serialization.CustomerSerializer;
import serialization.EmployeeSerializer;



public class Utility {

    public static int calculateLastId() {
        CustomerSerializer customerSerializer = CustomerSerializer.getInstance(); // Get per-thread instance
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
        EmployeeSerializer employeeSerializer = new EmployeeSerializer(Servers.currentHandler.get().getBranchClientHandler()); // Get per-thread instance
        Branch branch = Servers.currentHandler.get().getBranchClientHandler();
        String path = branch.getEmployeeFilePath();
        if (path == null) {
            path = util.Constants.EMPLOYEE_FILE;
        }

        File employeeFile = new File(path);
        if (!employeeFile.exists()) {
            try {
                employeeSerializer.saveEmployee(
                    new Employee(0, "administrator", "administrator", "admin@admin.com", "admin", "admin", "00000000", Role.ADMIN, new Branch("ALL_BRANCHES"))
                );
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error saving employee: " + e.getMessage());
            }
        }
    }
}
