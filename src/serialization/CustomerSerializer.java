package serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import main.Servers;
import model.Branch;
import model.customer.Customer;

public class CustomerSerializer extends CustomSerializer { 

    private String customerDataFile;
    private Branch branch;

    private static final ThreadLocal<CustomerSerializer> instance =
        ThreadLocal.withInitial(() -> {
            Branch branch = Servers.currentHandler.get().getBranchClientHandler();
            return new CustomerSerializer(branch);
        });

    // Call this method to get the instance for the current thread and linking late init branch
    public static CustomerSerializer getInstance() {
    CustomerSerializer serializer = instance.get();
    Branch currentBranch = Servers.currentHandler.get().getBranchClientHandler();
    //linking late init branch if not set yet
    if (serializer.branch == null && currentBranch != null) {
        serializer.branch = currentBranch;
        serializer.setFilesPaths();
    }
    return serializer;
}

//According to present code, when calling this constructor branch is always null. Udpate it in getInstance after getting currentBranch from Servers
    private CustomerSerializer(Branch branch) {
        this.branch = branch;
        setFilesPaths();
    }


    private void setFilesPaths() {
       if (branch != null) {
            this.customerDataFile = branch.getCustomerFilePath();
            System.out.println("Current branch in CustomerSerializer: " + branch.getName());
        } else {
            this.customerDataFile = util.Constants.CUSTOMER_DATA_FILE;
            System.out.println("No current branch set in CustomerSerializer.");
        }
    }

    public void saveCustomer(Customer customer) throws IOException, ClassNotFoundException {
        List<Customer> customers;
        // Try to load existing customers, or create a new list if file doesn't exist
        try {
            customers = loadCustomerList();
        } catch (IOException | ClassNotFoundException e) {
            customers = new java.util.ArrayList<>();
        }
        customers.add(customer);
        saveCustomerList(customers);
    }
    
    public Customer loadCustomer(int customer_id) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("customer_" + customer_id + ".ser"))) {
            return (Customer) in.readObject();
        }
    }

    public Customer loadCustomerByEmail(String email) throws IOException, ClassNotFoundException {
        List<Customer> customers = loadCustomerList();
        for (Customer cust : customers) {
            if (cust.getEmail().equalsIgnoreCase(email)) {
                return cust;
            }
        }
        throw new IOException("Customer with email " + email + " not found.");
    }

    public void deleteCustomer(int customer_id) throws IOException, ClassNotFoundException {
       try {
           List<Customer> customers = loadCustomerList();
           customers.removeIf(c -> c.getId() == customer_id);
           saveCustomerList(customers);
       } catch (IOException e) {
           System.err.println("Error loading customer list: " + e.getMessage());
       }
    }

    public void saveCustomerList(List<Customer> customers) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(customerDataFile))) {
            out.writeObject(customers);
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Customer> loadCustomerList() throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(customerDataFile))) {
            return (List<Customer>) in.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            // If the file does not exist or is empty, return an empty list
            System.err.println("There is no customer data available. Please add customers first.");
            return new java.util.ArrayList<>();
        }
    }
    
}