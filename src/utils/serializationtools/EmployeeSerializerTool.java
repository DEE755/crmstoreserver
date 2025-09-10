package utils.serializationtools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import model.Employee;
import utils.Constants;
import utils.JSONObject;
import utils.TypeConverter;

public class EmployeeSerializerTool {
    
    public void saveEmployee(Employee employee, String filename) throws IOException, ClassNotFoundException {
        List<Employee> employees;
        // Try to load existing employees, or create a new list if file doesn't exist
        try {
            employees = loadEmployeeList(filename);
        } catch (IOException | ClassNotFoundException e) {
            employees = new java.util.ArrayList<>();
        }
        employees.add(employee);
        saveEmployeeList(employees , filename);
    }

    public Employee loadEmployee(int employee_id) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("employee_" + employee_id + ".ser"))) {
            return (Employee) in.readObject();
        }
    }

    public void saveEmployeeList(List<Employee> employees, String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(employees);
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Employee> loadEmployeeList(Socket socket) throws IOException, ClassNotFoundException {
        try {
            JSONObject json = TypeConverter.employeeToJSON(null)
            socket.getOutputStream().write("Loading employee list...\n".getBytes());
            socket.getOutputStream().flush();
        }
        catch (IOException | ClassNotFoundException e) {
            // If the file does not exist or is empty, return an empty list
            System.err.println("There is no employee data available. Please add employees first.");
            return new java.util.ArrayList<>();
        }
    }

    public void createFileIfNotExists() {
        File employeeFile = new File(Constants.EMPLOYEE_FILE);
        if (!employeeFile.exists()) {
            try {
                EmployeeSerializerTool employeeSerializerTool = new EmployeeSerializerTool();
                employeeSerializerTool.saveEmployee(
                    new Employee(0, "administrator", "admin@admin.com", "admin", "admin", "00000000"),
                    Constants.EMPLOYEE_FILE
                );
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error saving employee: " + e.getMessage());
            }
        }
    }
}