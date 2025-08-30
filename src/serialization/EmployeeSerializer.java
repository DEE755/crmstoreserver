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
    public List<Employee> loadEmployeeList(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<Employee>) in.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            // If the file does not exist or is empty, return an empty list
            System.err.println("There is no employee data available. Please add employees first.");
            return new java.util.ArrayList<>();
        }
    }


    public void sendEmployee(Employee employee, Socket clientSocket) {
            ObjectOutputStream outputObject = null;
            try {
                outputObject = new ObjectOutputStream(clientSocket.getOutputStream());
                outputObject.writeObject(employee);
                outputObject.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        finally {
            if (outputObject != null) try { outputObject.close(); } catch (IOException ignored) {}
        }
    }
}