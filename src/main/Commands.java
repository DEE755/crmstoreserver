package main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Set;
import login.LoginHandler;
import model.Branch;
import model.ChatSession;
import model.Employee;
import model.Sale;
import model.customer.Customer;
import model.inventory.StockItem;
import serialization.ChatSessionSerializer;
import serialization.CustomerSerializer;
import serialization.EmployeeSerializer;
import serialization.SalesSerializer;
import serialization.StockItemSerializer;


public class Commands extends Servers {
private static EmployeeSerializer employeeSerializer = null;
private static CustomerSerializer customerSerializer = null;
private static StockItemSerializer stockItemSerializer = null;
private static SalesSerializer salesSerializer = null;

private static ChatSessionSerializer chatSessionSerializer = null;
private static serialization.Logger logger;
private Branch associatedBranch = null;
Set<Branch> existingBranches = null;


private static LoginHandler loginHandler = null;
private Socket clientSocket;

    public Commands(Socket clientSocket) {
        //one per instance
        loginHandler = new LoginHandler();
        this.clientSocket = clientSocket;

        //Singleton instances of serializers
            if (employeeSerializer == null){
                employeeSerializer = new EmployeeSerializer(Servers.currentHandler.get().getBranchClientHandler());
            }

            if (customerSerializer == null) {
                customerSerializer = CustomerSerializer.getInstance();
            }

            if (stockItemSerializer == null) {
                stockItemSerializer = StockItemSerializer.getInstance();
            }
            if (salesSerializer == null) {
                salesSerializer = SalesSerializer.getInstance();
            }


             if (chatSessionSerializer == null) {
                chatSessionSerializer = ChatSessionSerializer.getInstance();
            }

           
            if (logger == null) {
                logger = serialization.Logger.getInstance();
            }

            associatedBranch = Servers.currentHandler.get().getBranchClientHandler();

    }

public void refreshAssociatedBranch(Branch branch) {
    associatedBranch = Servers.currentHandler.get().getBranchClientHandler();
}

    void handleCommand(String commandWithArgs) throws IOException, ClassNotFoundException {

        

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
                        logger.log("CONNECTION: " + employee.getBranch().getName() + ": " + employee.getFullName() + " logged in.");
                        clientSocket.getOutputStream().write("SUCCESS\n".getBytes());
                        clientSocket.getOutputStream().flush();
                        employeeSerializer.sendEmployeeToClient(employee, clientSocket);
                        Servers.currentHandler.get().getBranchClientHandler().setConnectedEmployee(employee);

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

                    logger.log("ADDED: "+ "EMPLOYEE" + newEmployee.getFullName() + " (ID: " + newEmployee.getId() + ")" +" at branch "+associatedBranch.getName());

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

                    logger.log("ADDED: " +associatedBranch.getName() + " deleted Employee ID: " + employeeId);
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

                    logger.log("ADDED: " + associatedBranch.getName() + " deleted Customer ID: " + customerId);

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
                    logger.log("ADDED:" + associatedBranch.getName() + " added Customer: " + customer.getFullName() + " (ID: " + customer.getId() + ")" +" at branch "+associatedBranch.getName());
                    clientSocket.getOutputStream().write("SUCCESS\n".getBytes());
                    clientSocket.getOutputStream().flush();
                } catch (Exception ex) {
                    System.err.println("Error adding customer: " + ex.getMessage());
                }

                break;
            }
        } 

        case "AddItem": {
            // Example: AddItem Chair 34 200.89 18607 MISC
            if (parts.length == 7) {
            try {
                String name = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                double price = Double.parseDouble(parts[3]);
                int itemId = Integer.parseInt(parts[4]);
                StockItem.Category category = StockItem.Category.valueOf(parts[5].toUpperCase());
                String correspondingBranch = parts[6];

                StockItem newItem = new StockItem( name,itemId, quantity, price, category, correspondingBranch);

                // Save item using StockItemSerializer
                stockItemSerializer.saveStockItem(newItem);

                System.out.println("StockItem added: " + newItem);
                logger.log("ADDED:" + associatedBranch.getName() + " added StockItem: " + newItem.getName() + " (ID: " + newItem.getId() + ")");
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
                logger.log("ADDED:" + associatedBranch.getName() + " deleted StockItem: " + itemId);
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
                logger.log("STOCKS:" +  associatedBranch.getName() + " StockItem ID " + itemId + " quantity modified to " + newQuantity);
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

        case "BuyItem": {
           
            if (parts.length != 3) {
                System.err.println("Invalid BuyItem command.");
                break;
            }
            int itemId = Integer.parseInt(parts[1]);
            int quantity = Integer.parseInt(parts[2]);

            StockItem itemToBuy = stockItemSerializer.loadStockItemById(itemId);
            itemToBuy.setQuantity(itemToBuy.getQuantity() + quantity);
            logger.log("STOCKS:" + "BUYING" + associatedBranch.getName() + " Bought " + quantity + " of Item ID " + itemId + " new quantity: " + itemToBuy.getQuantity());
            // Update stock item quantity
            stockItemSerializer.modifyStockItemQuantity(itemId, itemToBuy.getQuantity());
            clientSocket.getOutputStream().write("SUCCESS\n".getBytes());
            clientSocket.getOutputStream().flush();

            break;
        }


        case "SubmitSale": {

            String customerEmail = parts[1];
            int itemId = Integer.parseInt(parts[2]);
            String sellerEmail = parts[3];
            double salePrice = Double.parseDouble(parts[4]);
            int quantitySold = Integer.parseInt(parts[5]);

           Customer buyer = customerSerializer.loadCustomerByEmail(customerEmail);
           StockItem itemSold = stockItemSerializer.loadStockItemById(itemId);
           Employee seller = employeeSerializer.loadEmployeeByEmail(sellerEmail);
           

            
            // Create Sale object
           
            Sale sale = new Sale(buyer, itemSold, seller, salePrice, quantitySold);

            //salesSerializer.serializeSalesData(sale, "sales_data.ser");

            logger.log("SALE:" + " Sale submitted: " + sale.toString()+ " at branch "+associatedBranch.getName());

            stockItemSerializer.modifyStockItemQuantity(itemSold.getId(), itemSold.getQuantity() - quantitySold);

            clientSocket.getOutputStream().write("SUCCESS\n".getBytes());
            clientSocket.getOutputStream().flush();
           
            break;
        }

        case "Logout": {
            try {
                logger.log("CONNECTION" + associatedBranch.getName() +" " +associatedBranch.getConnectedEmployee().getFullName() + " logged out.");
                System.out.println("Logging out...");
                loginHandler.setLoggedIn(false);
                Servers.currentHandler.get().getBranchClientHandler().setConnectionStatus(false);
                Servers.connectedBranches.remove(Servers.currentHandler.get().getBranchClientHandler());
                
                clientSocket.getOutputStream().write("SUCCESS\n".getBytes());
                clientSocket.getOutputStream().flush();
                clientSocket.close();
            } catch (Exception e) {
                System.err.println("Error during logout: " + e.getMessage());
            }
            break;
        }

        case "ListBranches": {
       
            try
            {
                //updates the list of branches to know if connected or not and sends all existing with right info to the client
                existingBranches = updateAndListAllBranches();

                System.out.println("existingBranches : ");
                existingBranches.forEach(branch -> System.out.println(" - " + branch.getName()+ " ID: "+branch.getId()+" connected: "+branch.isConnected() + (branch.getConnectedEmployee() != null ? " Employee: "+branch.getConnectedEmployee().getFullName() : "")));
            }
            catch (Exception e){
                System.err.println("g branches: " + e.getMessage());
            }

            break;
            

        }

        case "StartChatSession": { // StartChatSession (destination)ID Hello, I need assistance with an order.
            //the source is the associated branch of this client handler
            try{
                //Sent an updated list to the client wrapped in SUCCESS/ENDLIST
            if (parts.length < 3) {
                System.err.println("Invalid StartChatSession command.");
                break;
            }
             if (existingBranches == null || existingBranches.isEmpty()) {
                existingBranches = updateAndListAllBranches();
                if (existingBranches.isEmpty()) {
                    clientSocket.getOutputStream().write("EMPTY\n".getBytes());
                    clientSocket.getOutputStream().flush();
                    System.err.println("No existing branches found.");
                    break;
                }
            }

            
            int targetBranchId = Integer.parseInt(parts[1]);
            String message = commandWithArgs.substring(commandWithArgs.indexOf(parts[2])); //start after the branch id

            Branch targetBranch = Branch.findBranchById(targetBranchId, existingBranches);

            ChatSession chatSession = new ChatSession(associatedBranch, targetBranch);
            chatSession.addMessage(message);

            clientSocket.getOutputStream().flush();

           sendMessageToParticipants(chatSession);
            logger.log("MESSAGE:" + associatedBranch.getConnectedEmployee().getFullName().replace(" ", "-") + "@" + associatedBranch.getName() + " sent to " + targetBranch.getName() + ": " + message);
            clientSocket.getOutputStream().write("SUCCESS\n".getBytes());


        }        catch (Exception e){
            System.err.println("Error starting chat session: " + e.getMessage());
  

        }

        
        break;
    }

        case "ListChat": {
        int branchId;
        try {
            branchId = Integer.parseInt(parts[1]);
        } catch (Exception e) {
            clientSocket.getOutputStream().write("ERROR Invalid branch ID\n".getBytes());
            return;
        }

        File chatFile = new File("chat_sessions/" + branchId + ".txt");
        if (!chatFile.exists() || chatFile.length() == 0) {
            clientSocket.getOutputStream().write("EMPTY\n".getBytes());
            return;
        }

        clientSocket.getOutputStream().write("SUCCESS\n".getBytes());
        try (BufferedReader chatReader = new BufferedReader(new FileReader(chatFile))) {
            String chatLine;
            while ((chatLine = chatReader.readLine()) != null) {
                clientSocket.getOutputStream().write((chatLine + "\n").getBytes());
            }
        } catch (IOException e) {
            clientSocket.getOutputStream().write("ERROR Reading chat file\n".getBytes());
            return;
        }
        clientSocket.getOutputStream().write("ENDLIST\n".getBytes());
        break;
    }

   

    case "GetLogs": {
    File logFile = new File("all_activity.log");
    if (!logFile.exists() || logFile.length() == 0) {
        clientSocket.getOutputStream().write("EMPTY\n".getBytes());
        return;
    }

    clientSocket.getOutputStream().write("SUCCESS\n".getBytes());
    try (BufferedReader logReader = new BufferedReader(new FileReader(logFile))) {
        String logLine;
        while ((logLine = logReader.readLine()) != null) {
            clientSocket.getOutputStream().write((logLine + "\n").getBytes());
        }
    } catch (IOException e) {
        clientSocket.getOutputStream().write("ERROR Reading log file\n".getBytes());
        return;
    }
    clientSocket.getOutputStream().write("ENDLIST\n".getBytes());
    break;
}




        default:
        
            try {
                clientSocket.getOutputStream().write("UNKNOWN COMMAND\n".getBytes());
                clientSocket.getOutputStream().flush();
            } catch (IOException | NullPointerException e) {
                System.err.println("Error sending unknown command response: " + e.getMessage());
            }
            
            }
        }
    



    private void sendMessageToParticipants(ChatSession chatSession) throws IOException {
        
       if (!chatSession.getDestinationBranch().isConnected()){
        //if the destination branch is not connected, save the chat session to file for later retrieval
        System.out.println("Destination branch "+chatSession.getDestinationBranch().getName()+" is not connected, saving chat session to file.");
           chatSessionSerializer.createBranchChatTextFileIfNeededAndWrite(chatSession);
       }

    else {
         boolean found = false;
        //send the message to the destination branch client handler if connected
        for (ClientHandler handler : Servers.clientHandlers) {
           
            System.err.println("Checking handler for branch ID: " + (handler.branchClientHandler != null ? handler.branchClientHandler.getId() : "null"));
            if (handler.branchClientHandler != null && handler.branchClientHandler.getId() == chatSession.getDestinationBranch().getId()) {
                handler.clientSocket.getOutputStream().write(("ChatMessage " + chatSession.getSourceBranchEmployee().getFullName().replace(" ", "-") + "@" + chatSession.getSourceBranch().getName() + " " + chatSession.getNextMessage() + "\n").getBytes());
                handler.clientSocket.getOutputStream().flush();
                System.err.println("Message sent to branch ID: " + chatSession.getDestinationBranch().getId() + " Message: " + chatSession.getNextMessage());
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IOException("No client handler found for destination branch ID: " + chatSession.getDestinationBranch().getId());
        }
    }
}

    //updates the list of branches to know if connected or not and sends all existing with right info to the client
    private Set<Branch> updateAndListAllBranches() throws IOException
    {
        Set<Branch> existingBranches = Branch.detectExistingBranches();
        String branchesText = util.TypeConverter.branchSetToText(existingBranches);
        
        String response = "SUCCESS\n" + branchesText + "ENDLIST\n";
        clientSocket.getOutputStream().write(response.getBytes());
        
        clientSocket.getOutputStream().flush();
        return existingBranches;

    }

  
}
