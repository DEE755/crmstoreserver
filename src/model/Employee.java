package model;
public class Employee implements java.io.Serializable {
    private String name;
    private int id;
    private String email; 
    private String username;
    private String password;
    private String phoneNumber;

    static int idCounter = 0;

    public Employee(String name, String email, String username, String password, String phoneNumber) {
        this.name = name;
        this.email = email; // Default email format
        this.id = ++idCounter;
        this.username = username;
        this.password = password;
        this.phoneNumber =phoneNumber; // Default phone number format
    }

    public String getName() {
        return name;
    }

     public String getEmail() {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}

