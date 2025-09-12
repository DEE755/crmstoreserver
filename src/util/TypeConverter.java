package util;

import java.util.List;
import model.Branch;
import model.Employee;
import model.customer.Customer;
import model.customer.NewCustomer;
import model.customer.ReturningCustomer;
import model.customer.VIPCustomer;

public class TypeConverter {

public static Employee stringToEmployee(String employeeString) {
    String[] parts = employeeString.split(" ");
    if (parts.length != 1 && parts.length != 9) {
        throw new IllegalArgumentException("Invalid employee string: " + employeeString);
    }
    System.err.println("Parts length: " + parts.length);
    int id = Integer.parseInt(parts[0]);
    String firstName = parts[1];
    String familyName = parts[2];
    String password = parts[3];
    String email = parts[4];
    String username = parts[5];
    String phoneNumber = parts[6];
    Employee.Role role = Employee.Role.valueOf(parts[7]);
    Branch branch = new Branch(parts[8]);
    return new Employee(id, firstName, familyName, email, username, password, phoneNumber, role, branch);
    }






public static String employeeListToText(List<Employee> employees) {
    StringBuilder sb = new StringBuilder();
    for (Employee employee : employees) {
        
        // Example: comma-separated values and newline for each employee (allow multiple readline() calls until empty)
        sb.append(employee.getId()).append(" ")
          .append(employee.getFirstName()).append(" ")
          .append(employee.getFamilyName()).append(" ")
          .append(employee.getPassword()).append(" ")
          .append(employee.getEmail()).append(" ")
          .append(employee.getUsername()).append(" ")
          .append(employee.getPhoneNumber()).append(" ")
          .append(employee.getRole().toString()).append(" ")
          .append(employee.getBranch().getName()).append("\n");


    }
    return sb.toString();
}




public static String customerListToText(List<Customer> customers) {
    StringBuilder sb = new StringBuilder();
    for (Customer customer : customers) {
        // Example: comma-separated values and newline for each customer (allow multiple readline() calls until empty)
        sb.append(customer.getId()).append(" ")
          .append(customer.getFullName()).append(" ")
          .append(customer.getEmail()).append(" ")
          .append(customer.getPhoneNumber()).append(" ")
          .append(customer.getDiscount()).append(" ")
          .append(customer.getClass().getSimpleName())
          .append("\n");
    }

    return sb.toString();
}

public static Customer stringToCustomer(String customerInfoString) {
    Customer newCustomer=null;
    String[] parts = customerInfoString.split(" ");
    if (parts.length != 1 && parts.length != 7) {
        throw new IllegalArgumentException("Invalid customer string: " + customerInfoString);
    }
    System.err.println("Parts length: " + parts.length);
    int id = Integer.parseInt(parts[0]);
    String firstName = parts[1];
    String familyName = parts[2];
    String email = parts[3];
    String phoneNumber = parts[4];
    double discount = Double.parseDouble(parts[5]);

    switch (parts[6]) {
        case "NewCustomer":
            newCustomer = new NewCustomer(id, firstName, familyName, email, phoneNumber, discount);
            break;
        case "ReturningCustomer":
            newCustomer = new ReturningCustomer(id, firstName, familyName, email, phoneNumber, discount);
            break;
        case "VIPCustomer":
            newCustomer = new VIPCustomer(id, firstName, familyName, email, phoneNumber, discount);
            break;
        default:
            throw new IllegalArgumentException("Unknown customer type: " + parts[6]);
    }
    return newCustomer;

}

}
