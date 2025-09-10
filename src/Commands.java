import java.net.Socket;
import java.util.List;
import login.LoginHandler;
import model.Employee;
import model.customer.Customer;
import serialization.CustomerSerializer;
import serialization.EmployeeSerializer;


public class Commands {
private static EmployeeSerializer employeeSerializer = null;
private static CustomerSerializer customerSerializer = null;

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

            if (customerSerializer == null) {
                customerSerializer = new CustomerSerializer();
            }

    }


    void handleCommand(String commandWithArgs) {

        int firstSpace = commandWithArgs.indexOf(' ');
        String firstWord, commandOnly;
        if (firstSpace != -1) {
            firstWord = commandWithArgs.substring(0, firstSpace);
        } else {
            firstWord = commandWithArgs; // No space found
        }
        commandOnly = firstWord;

       switch (commandOnly) {
            case "Login":
        {
            
            String[] parts = commandWithArgs.split(" ");
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

                break;

            }
        }
        case "AddEmployee": {

            String[] parts = commandWithArgs.split(" ");

            if (parts.length == 7) {
                try {

                    //start reading after "AddEmployee+ 'space'"
                    Employee newEmployee = util.TypeConverter.stringToEmployee(commandWithArgs.substring(12)); 
                    
                    // Add the employee with the serializer
                    employeeSerializer.saveEmployeeToLocalFile(newEmployee);
                    System.out.println("Employee added: " + newEmployee);

                    // Send a confirmation to the client
                    clientSocket.getOutputStream().write("SUCCESS\n".getBytes());
                    clientSocket.getOutputStream().flush();
                } catch (Exception ex) {
                    System.err.println("Error adding employee: " + ex.getMessage());
                }

                break;

            }
        }

        case "ListEmployees": {
            try {
                System.out.println("Listing employees...");
                // Load the employee list
                List<Employee> employees = employeeSerializer.loadEmployeeList();

                // Convert to text format
                String employeesText = util.TypeConverter.employeeListToText(employees);

                System.out.println("Employees:\n" + employeesText);

                // Send to client
                String response = "SUCCESS\n" + employeesText + "\n";
                clientSocket.getOutputStream().write(response.getBytes());
                clientSocket.getOutputStream().flush();

            } catch (Exception ex) {
                System.err.println("Error listing employees: " + ex.getMessage());
            }
            break;
        }


        case "ListCustomers": {
            try {
                System.out.println("Listing customers...");
                // Load the customer list
                List<Customer> customers = customerSerializer.loadCustomerList();

                if (customers.isEmpty()) {
                    System.out.println("No customers found.");
                    clientSocket.getOutputStream().write("EMPTY\n".getBytes());
                    clientSocket.getOutputStream().flush();
                    break;
                }
                // Convert to text format
                String customersText = util.TypeConverter.customerListToText(customers);

                System.out.println("Customers:\n" + customersText);

                // Send to client
                String response = "SUCCESS\n" + customersText + "\n" + "ENDLIST\n";
                clientSocket.getOutputStream().write(response.getBytes());
                clientSocket.getOutputStream().flush();

            } catch (Exception ex) {
                System.err.println("Error listing customers: " + ex.getMessage());
            }
            break;
        }


        case "AddCustomer": {

            String[] parts = commandWithArgs.split(" ");

            if (parts.length == 7) {
                try {

                    //start reading after "AddCustomer+ 'space'"
                    Customer customer = util.TypeConverter.stringToCustomer(commandWithArgs.substring(12));

                    // Add the customer with the serializer
                    customerSerializer.saveCustomer(customer);
                    System.out.println("Customer added: " + customer);

                    // Send a confirmation to the client
                    clientSocket.getOutputStream().write("SUCCESS\n".getBytes());
                    clientSocket.getOutputStream().flush();
                } catch (Exception ex) {
                    System.err.println("Error adding employee: " + ex.getMessage());
                }

                break;

            }
        }

        


        default:
            try {
                clientSocket.getOutputStream().write("UNKNOWN COMMAND\n".getBytes());
                clientSocket.getOutputStream().flush();
            } catch (Exception e) {
                System.err.println("Error sending unknown command response: " + e.getMessage());
            }
       


        }
    }
}
