package util;

import java.util.List;
import model.Employee;
import model.customer.Customer;
import model.customer.NewCustomer;
import model.customer.ReturningCustomer;
import model.customer.VIPCustomer;

public class TypeConverter {

    public static Employee JSONToEmployee(JSONObject json) {
    Employee employee = new Employee(
        json.getInt("id"),
        json.getString("name"),
        json.getString("username"),
        json.getString("password"),
        json.getString("email"),
        json.getString("phoneNumber")
    );

    return employee;
    }


public static JSONObject employeeToJSON(Employee employee) {
    JSONObject json = new JSONObject();
    json.put("id", employee.getId());
    json.put("name", employee.getName());
    json.put("username", employee.getUsername());
    json.put("password", employee.getPassword());
    json.put("email", employee.getEmail());
    json.put("phoneNumber", employee.getPhoneNumber());
    return json;
}

public static Employee stringToEmployee(String str) {
    Employee newEmployee;
    String[] parts = str.split(" ");

    if (parts.length != 6) {
        throw new IllegalArgumentException("Invalid employee string format");
    }
    else {
        newEmployee= new Employee(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3], parts[4], parts[5]);
    }

    return newEmployee;

}






public static String employeeListToText(List<Employee> employees) {
    StringBuilder sb = new StringBuilder();
    for (Employee employee : employees) {
        // Example: comma-separated values and newline for each employee (allow multiple readline() calls until empty)
        sb.append(employee.getId()).append(" ")
          .append(employee.getName()).append(" ")
          .append(employee.getUsername()).append(" ")
          .append(employee.getPassword()).append(" ")
          .append(employee.getEmail()).append(" ")
          .append(employee.getPhoneNumber())
          .append("\n");

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
