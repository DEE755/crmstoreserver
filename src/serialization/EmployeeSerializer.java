package serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import model.Employee;

public class EmployeeSerializer {

    private static final EmployeeSerializer instance = new EmployeeSerializer();

    public static EmployeeSerializer getInstance() {
        return instance;
    }

    public void saveEmployeeToLocalFile(Employee employee) throws IOException, ClassNotFoundException {
        List<Employee> employees;
        // Try to load existing employees, or create a new list if file doesn't exist
        try {
            employees = loadEmployeeList();
        } catch (IOException | ClassNotFoundException e) {
            employees = new java.util.ArrayList<>();
        }
        employees.add(employee);
        saveEmployeeList(employees);
    }

    public Employee loadEmployee(int employee_id) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("employee_" + employee_id + ".ser"))) {
            return (Employee) in.readObject();
        }
    }

    public void saveEmployeeList(List<Employee> employees) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(util.Constants.EMPLOYEE_FILE))) {
            out.writeObject(employees);
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Employee> loadEmployeeList() throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream(util.Constants.EMPLOYEE_FILE);
        try (ObjectInputStream in = new ObjectInputStream(file)) {
            System.out.println("Loading employee list from " + util.Constants.EMPLOYEE_FILE);
            return (List<Employee>) in.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            // If the file does not exist or is empty, return an empty list
            System.err.println("There is no employee data available. Please add employees first. File path: " + new java.io.File(util.Constants.EMPLOYEE_FILE).getAbsolutePath());
            return new java.util.ArrayList<>();
        }
    }


    public void sendEmployeeToClient(Employee employee, Socket clientSocket) {
        
            System.out.println("Sending employee data...");
            try {
               String employeeJson = String.format(
            "{\"id\":%d,\"name\":\"%s\",\"username\":\"%s\",\"password\":\"%s\",\"email\":\"%s\",\"phoneNumber\":\"%s\"}",
            employee.getId(), employee.getName(), employee.getUsername(), employee.getPassword(),
            employee.getEmail(), employee.getPhoneNumber());
            clientSocket.getOutputStream().write("SUCCESS\n".getBytes());
            clientSocket.getOutputStream().flush();
            clientSocket.getOutputStream().write((employeeJson + "\n").getBytes());
            clientSocket.getOutputStream().flush();

            System.out.println("Employee data sent successfully.");
            } catch (Exception e) {
                System.err.println("Error sending employee data: " + e.getMessage());
                e.printStackTrace();
            }
        
    }


      public void saveEmployee(Employee employee) throws IOException, ClassNotFoundException {
        List<Employee> employees;
        // Try to load existing employees, or create a new list if file doesn't exist
        try {
            employees = loadEmployeeList();
        } catch (IOException | ClassNotFoundException e) {
            employees = new java.util.ArrayList<>();
        }
        employees.add(employee);
        saveEmployeeList(employees);
    }
}