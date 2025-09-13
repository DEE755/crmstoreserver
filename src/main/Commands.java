package main;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import login.LoginHandler;
import model.Employee;
import model.customer.Customer;
import model.inventory.StockItem;
import serialization.CustomerSerializer;
import serialization.EmployeeSerializer;
import serialization.StockItemSerializer;


public class Commands {
private static EmployeeSerializer employeeSerializer = null;
private static CustomerSerializer customerSerializer = null;
private static StockItemSerializer stockItemSerializer = null;


private static LoginHandler loginHandler = null;
private Socket clientSocket;
    public Commands(Socket clientSocket) {
        //one per instance
        loginHandler = new LoginHandler();
        this.clientSocket = clientSocket;

        //Singleton
            if (employeeSerializer == null){
                employeeSerializer = EmployeeSerializer.getInstance();
            }

            if (customerSerializer == null) {
                customerSerializer = CustomerSerializer.getInstance();
            }

            if (stockItemSerializer == null) {
                stockItemSerializer = StockItemSerializer.getInstance();
            }

    }


    void handleCommand(String commandWithArgs) throws IOException {

        String[] parts = commandWithArgs.split(" ");
        String commandOnly = parts[0]; // Extract the first word

       switch (commandOnly) {
            case "Login":
        {
            
            if (parts.length == 3) {
                try {
                    String username = parts[1];
                    String password = parts[2];

                    Employee employee = loginHandler.authenticate(username, password, employeeSerializer.loadEmployeeList());
                    if (employee != null) {
         
                        System.out.println("Login successful! Welcome " + employee.getFirstName());
                        loginHandler.setLoggedIn(true);
                        clientSocket.getOutputStream().write("SUCCESS\n".getBytes());
                        clientSocket.getOutputStream().flush();
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

            if (parts.length == 10) {
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
            else {
                try {
                    clientSocket.getOutputStream().write("INVALID ARGUMENTS\n".getBytes());
                    clientSocket.getOutputStream().flush();
                } catch (Exception e) {
                    System.err.println("Error sending invalid arguments response: " + e.getMessage());
                }
            }
        }

        case "ListEmployees": {
            if (parts.length > 2 && parts[1].equals("DELETEMODE")) {
                try {

                    //take the id after "DeleteEmployee "
                    int employeeId = Integer.parseInt(parts[2]);

                    // Delete the employee with the serializer
                    employeeSerializer.deleteEmployee(employeeId);
                    System.out.println("Employee deleted: " + employeeId);

                    // Send a confirmation to the client
                    clientSocket.getOutputStream().write("SUCCESS\n".getBytes());
                    clientSocket.getOutputStream().flush();
                } catch (Exception ex) {
                    System.err.println("Error deleting employee: " + ex.getMessage());
                }

            }

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

            if (parts.length > 2 && parts[1].equals("DELETEMODE")) {
                try {

                    //take the id after "DeleteCustomer "
                    int customerId = Integer.parseInt(parts[2]);

                    // Delete the customer with the serializer
                    customerSerializer.deleteCustomer(customerId);
                    System.out.println("Customer deleted: " + customerId);

                    // Send a confirmation to the client
                    clientSocket.getOutputStream().write("SUCCESS\n".getBytes());
                    clientSocket.getOutputStream().flush();
                } catch (Exception ex) {
                    System.err.println("Error deleting customer: " + ex.getMessage());
                }

            }

            try {
                System.out.println("Listing customers...");
                // Load the customer list
                List<Customer> customers = customerSerializer.loadCustomerList();

                if (customers.isEmpty()) {
                    clientSocket.getOutputStream().flush();
                    System.out.println("No customers found.");
                    clientSocket.getOutputStream().write("EMPTY\n".getBytes());
                    clientSocket.getOutputStream().flush();
                    break;
                }
                // Convert to text format
                String customersText = util.TypeConverter.customerListToText(customers);

                System.out.println("Customers:\n" + customersText);

                // Send to client
                String response = "SUCCESS\n" + customersText + "ENDLIST\n";
                clientSocket.getOutputStream().write(response.getBytes());
                clientSocket.getOutputStream().flush();

            } catch (Exception ex) {
                System.err.println("Error listing customers: " + ex.getMessage());
            }

            
            break;
        }


        case "AddCustomer": {

            parts = commandWithArgs.split(" ");

            if (parts.length == 8) {
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

        case "AddItem": {
            // Example: AddItem Chair 34 200.89 18607 MISC
            if (parts.length == 6) {
            try {
                String name = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                double price = Double.parseDouble(parts[3]);
                int itemId = Integer.parseInt(parts[4]);
                StockItem.Category category = StockItem.Category.valueOf(parts[5].toUpperCase());

                StockItem newItem = new StockItem( name,itemId, quantity, price, category);

                // Save item using StockItemSerializer
                stockItemSerializer.saveStockItem(newItem);

                System.out.println("StockItem added: " + newItem);
                clientSocket.getOutputStream().write("SUCCESS\n".getBytes());
                clientSocket.getOutputStream().flush();
            } catch (Exception ex) {
                System.err.println("Error adding StockItem: " + ex.getMessage());
                clientSocket.getOutputStream().write("ERROR\n".getBytes());
                clientSocket.getOutputStream().flush();
            }
            }
            break;}
            
            case "ListItems": {
            // Example: AddItems DELETEMODE 18607
            if (parts.length > 2 && parts[1].equals("DELETEMODE")) {
            try {
                int itemId = Integer.parseInt(parts[2]);
                stockItemSerializer.deleteStockItem(itemId);
                System.out.println("StockItem deleted: " + itemId);
                clientSocket.getOutputStream().write("SUCCESS\n".getBytes());
                clientSocket.getOutputStream().flush();
            } catch (Exception ex) {
                System.err.println("Error deleting StockItem: " + ex.getMessage());
                clientSocket.getOutputStream().write("ERROR\n".getBytes());
                clientSocket.getOutputStream().flush();
            }
            }
            else if (parts.length > 2 && parts[1].equals("EDITMODE")) {
            try {
                int itemId = Integer.parseInt(parts[2]);
                int newQuantity = Integer.parseInt(parts[3]);

                stockItemSerializer.modifyStockItemQuantity(itemId, newQuantity);
                clientSocket.getOutputStream().write(("SUCCESS\n").getBytes());
                clientSocket.getOutputStream().flush();
            } 
            catch (Exception ex) 
            {
                System.err.println("Error modifying StockItem quantity: " + ex.getMessage());
                clientSocket.getOutputStream().write("ERROR\n".getBytes());
                clientSocket.getOutputStream().flush();
            }
        }

             try {
                System.out.println("Listing items...");
                // Load the stock item list
                List<StockItem> stockItems = stockItemSerializer.loadStockItemList();

                if (stockItems.isEmpty()) {
                    clientSocket.getOutputStream().flush();
                    System.out.println("No stock items found.");
                    clientSocket.getOutputStream().write("EMPTY\n".getBytes());
                    clientSocket.getOutputStream().flush();
                    break;
                }
                // Convert to text format
                String stockItemsText = util.TypeConverter.stockItemListToText(stockItems);

                System.out.println("Stock Items:\n" + stockItemsText);

                // Send to client
                String response = "SUCCESS\n" + stockItemsText + "ENDLIST\n";
                clientSocket.getOutputStream().write(response.getBytes());
                clientSocket.getOutputStream().flush();

            } catch (Exception ex) {
                System.err.println("Error listing stock items: " + ex.getMessage());
            }

            
            break;
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
