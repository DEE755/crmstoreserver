package utils;

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

}
