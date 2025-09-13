package serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import main.Servers;
import model.Branch;
import model.Employee;

public class EmployeeSerializer extends CustomSerializer {
    
    private Branch branch;
    private String employeeDataFile;

    private static final ThreadLocal<EmployeeSerializer> instance =
        ThreadLocal.withInitial(() -> {
            Branch branch = Servers.currentBranch.get();
            return new EmployeeSerializer(branch);
        });

      // Call this method to get the instance for the current thread and linking late init branch
    public static EmployeeSerializer getInstance() {
    EmployeeSerializer serializer = instance.get();
    Branch currentBranch = Servers.currentBranch.get();
    //linking late init branch if not set yet
    if (serializer.branch == null && currentBranch != null) {
        serializer.branch = currentBranch;
        serializer.setFilesPaths();
    }
    return serializer;
}

//According to present code, when calling this constructor branch is always null. Udpate it in getInstance after getting currentBranch from Servers
    private EmployeeSerializer(Branch branch) {
        this.branch = branch;
        setFilesPaths();
    }

    private void setFilesPaths() {
        if (branch != null) {
            this.employeeDataFile = branch.getEmployeeFilePath();
            System.out.println("Current branch in EmployeeSerializer: " + branch.getName());
        } else {
            this.employeeDataFile = util.Constants.EMPLOYEE_FILE;
            System.out.println("No current branch set in EmployeeSerializer.");
        }
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
        List<Employee> employees = loadEmployeeList();
        for (Employee emp : employees) {
            if (emp.getId() == employee_id) {
                return emp;
            }
        }
        throw new IOException("Employee with ID " + employee_id + " not found.");
    }

    public void saveEmployeeList(List<Employee> employees) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(employeeDataFile))) {
            out.writeObject(employees);
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Employee> loadEmployeeList() throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream(employeeDataFile);
        try (ObjectInputStream in = new ObjectInputStream(file)) {
            System.out.println("Loading employee list from " + employeeDataFile);
            return (List<Employee>) in.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            // If the file does not exist or is empty, return an empty list
            System.err.println("There is no employee data available. Please add employees first. File path: " + new java.io.File(employeeDataFile).getAbsolutePath());
            return new java.util.ArrayList<>();
        }
    }



     public void sendEmployeeToClient(Employee employee, Socket clientSocket) {

            System.out.println("Sending employee data...");
            try {
                clientSocket.getOutputStream().write((employeeToString(employee) + "\n").getBytes());
                clientSocket.getOutputStream().flush();
            } catch (IOException e) {
                System.err.println("Error sending employee data: " + e.getMessage());
            }
    }

    public static String employeeToString(Employee employee) {
    String employeeString = employee.getId() + " " +
                            employee.getFirstName() + " " +
                            employee.getFamilyName() + " " +
                            employee.getPassword() + " " +
                            employee.getEmail() + " " +
                            employee.getUsername() + " " +
                            employee.getPhoneNumber() + " " +
                            employee.getRole().name() + " " +
                            employee.getBranch().getName();
    return employeeString;
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

      public void deleteEmployee(int employeeId) throws ClassNotFoundException {
         try {
           List<Employee> employees = loadEmployeeList();
           employees.removeIf(e -> e.getId() == employeeId);
           saveEmployeeList(employees);
       } catch (IOException e) {
           System.err.println("Error loading employee list: " + e.getMessage());
       }

        
      }
}