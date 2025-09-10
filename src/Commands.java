import java.net.Socket;
import java.util.List;
import login.LoginHandler;
import model.Employee;
import serialization.EmployeeSerializer;


public class Commands {
private static EmployeeSerializer employeeSerializer = null;
private static LoginHandler loginHandler = null;
private Socket clientSocket;
    public Commands(Socket clientSocket) {
        //one per instance
        loginHandler = new LoginHandler();
        this.clientSocket = clientSocket;

        //Singleton
        if (employeeSerializer == null){
            employeeSerializer = new EmployeeSerializer();
        }

    }


    void handleCommand(String command) {
        if (command.contains("Login"))
        {
            
            String[] parts = command.split(" ");
            if (parts.length == 3) {
                try {
                    String username = parts[1];
                    String password = parts[2];

                    Employee employee = loginHandler.authenticate(username, password, employeeSerializer.loadEmployeeList());
                    if (employee != null) {
                        System.out.println("Login successful! Welcome " + employee.getName());
                        loginHandler.setLoggedIn(true);
                        employeeSerializer.sendEmployeeToClient(employee, clientSocket);

                    } else {
                        System.out.println("Login failed. Please check your credentials.");
                        clientSocket.getOutputStream().write("Login failed\n".getBytes());
                        clientSocket.getOutputStream().flush();

                    }
                        
                    

                } catch (Exception ex) {
                    System.err.println("Error during login: " + ex.getMessage());
                }

            }
        }
        else if (command.contains("AddEmployee")) {

            String[] parts = command.split(" ");

            if (parts.length == 7) {
                try {

                    //start reading after "AddEmployee+ 'space'"
                    Employee newEmployee = utils.TypeConverter.stringToEmployee(command.substring(12)); 
                    
                    // Add the employee with the serializer
                    employeeSerializer.saveEmployeeToLocalFile(newEmployee);
                    System.out.println("Employee added: " + newEmployee);

                    // Send a confirmation to the client
                    clientSocket.getOutputStream().write("SUCCESS\n".getBytes());
                    clientSocket.getOutputStream().flush();
                } catch (Exception ex) {
                    System.err.println("Error adding employee: " + ex.getMessage());
                }
                
            }
        }

        else if (command.contains("ListEmployees")) {
            try {
                System.out.println("Listing employees...");
                // Load the employee list
                List<Employee> employees = employeeSerializer.loadEmployeeList();

                // Convert to text format
                String employeesText = utils.TypeConverter.employeeListToText(employees);

                System.out.println("Employees:\n" + employeesText);

                // Send to client
                String response = "SUCCESS\n" + employeesText + "\n";
                clientSocket.getOutputStream().write(response.getBytes());
                clientSocket.getOutputStream().flush();

            } catch (Exception ex) {
                System.err.println("Error listing employees: " + ex.getMessage());
            }
        }
        else {
            try {
                clientSocket.getOutputStream().write("UNKNOWN COMMAND\n".getBytes());
                clientSocket.getOutputStream().flush();
            } catch (Exception e) {
                System.err.println("Error sending unknown command response: " + e.getMessage());
            }
        }


    }
}
