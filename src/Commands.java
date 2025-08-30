import java.net.Socket;
import login.LoginHandler;
import model.Employee;
import serialization.EmployeeSerializer;



public class Commands {
private static EmployeeSerializer employeeSerializer = null;
private static LoginHandler loginHandler = null;

    public Commands() {
        //one per instance
        loginHandler = new LoginHandler();

        //Singleton
        if (employeeSerializer == null){
            employeeSerializer = new EmployeeSerializer();
        }

    }


    void handleCommand(String command, Socket clientSocket) {
        if (command.contains("Login"))
        {
            
            String[] parts = command.split(" ");
            if (parts.length == 3) {
                try {
                    String username = parts[1];
                    String password = parts[2];
                    Employee employee = loginHandler.authenticate(username, password, employeeSerializer.loadEmployeeList("employees.ser"));
                    if (employee != null) {
                        System.out.println("Login successful! Welcome " + employee.getName());
                        loginHandler.setLoggedIn(true);
                        employeeSerializer.sendEmployee(employee, clientSocket);

                    } else {
                        System.out.println("Login failed. Please check your credentials.");
                    
                    }

                } catch (Exception ex) {
                    System.err.println("Error during login: " + ex.getMessage());
                }

            }
        }


    }
}
