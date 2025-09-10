package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import model.Employee;

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



public static JSONObject textEmployeeFileToJSONObject() {
    JSONObject jsonObject = new JSONObject();
    try (BufferedReader reader = new BufferedReader(new FileReader("employees.ser"))) {
        String line;
        int index = 0;
        while ((line = reader.readLine()) != null) {
            Employee employee = stringToEmployee(line);
            jsonObject.put(String.valueOf(employee.getId()), employeeToJSON(employee));
            index++;
        }
    } catch (Exception e) {
        System.err.println("Error converting text file to JSONObject: " + e.getMessage());
    }
    return jsonObject;
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
          .append(employee.getPhoneNumber()).append("\n");
    }
    return sb.toString();
}
}
