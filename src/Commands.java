import java.net.Socket;
import login.LoginHandler;
import model.Employee;
import serialization.EmployeeSerializer;
import utils.JSONObject;


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
                        clientSocket.getOutputStream().write("Login failed".getBytes());
                        clientSocket.getOutputStream().flush();

                    }
                        
                    

                } catch (Exception ex) {
                    System.err.println("Error during login: " + ex.getMessage());
                }

            }
            else if (command.contains("Add Employee")) {
                try {
                    String employeeinfo = clientSocket.getInputStream().toString();
                    JSONObject jsonObject = new JSONObject(employeeinfo);
                    Employee newEmployee = utils.TypeConverter.JSONToEmployee(jsonObject);
                    
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


    }
}
