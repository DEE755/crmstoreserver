package serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import model.customer.Customer;

public class CustomerSerializer {
    
    public void saveCustomer(Customer customer, String filename) throws IOException, ClassNotFoundException {
        List<Customer> customers;
        // Try to load existing customers, or create a new list if file doesn't exist
        try {
            customers = loadCustomerList(filename);
        } catch (IOException | ClassNotFoundException e) {
            customers = new java.util.ArrayList<>();
        }
        customers.add(customer);
        saveCustomerList(customers, filename);
    }
    
    public Customer loadCustomer(int customer_id) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("customer_" + customer_id + ".ser"))) {
            return (Customer) in.readObject();
        }
    }

    public void deleteCustomer(int customer_id) throws IOException, ClassNotFoundException {
       try {
           List<Customer> customers = loadCustomerList("customers.ser");
           customers.removeIf(c -> c.getId() == customer_id);
           saveCustomerList(customers, "customers.ser");
       } catch (IOException e) {
           System.err.println("Error loading customer list: " + e.getMessage());
       }
    }

    public void saveCustomerList(List<Customer> customers, String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(customers);
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Customer> loadCustomerList(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<Customer>) in.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            // If the file does not exist or is empty, return an empty list
            System.err.println("There is no customer data available. Please add customers first.");
            return new java.util.ArrayList<>();
        }
    }
}