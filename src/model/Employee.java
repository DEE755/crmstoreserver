package model;
public class Employee implements java.io.Serializable {
    private String firstName;
    private String familyName;
    private int id;
    private String email; 
    private String username;
    private String password;
    private String phoneNumber;
    private Role role;
    private transient Branch branch;
    private String branchText;

    static int idCounter = 0;

    public Employee(String firstName, String familyName,String email, String username, String password, String phoneNumber, Role role, Branch branch) {
        this.firstName = firstName;
        this.familyName = familyName;
        this.email = email; // Default email format
        this.id = ++idCounter;
        this.username = username;
        this.password = password;
        this.phoneNumber =phoneNumber; // Default phone number format
        this.role = role;
        this.branch = branch;
        this.branchText = branch != null ? branch.getName() : "";
    }

     public Employee(int id, String firstName, String familyName, String email, String username, String password, String phoneNumber, Role role, Branch branch) {
         if (id > idCounter) {
             idCounter = id; // Ensure idCounter is always the max assigned ID
         }
        this.firstName = firstName;
        this.familyName = familyName;
        this.email = email; 
        this.id = id;
        this.username = username;
        this.password = password;
        this.phoneNumber =phoneNumber;
        this.role = role;
        this.branch = branch;
        this.branchText = branch != null ? branch.getName() : "";
    }


        public enum Role {
            SHIFT_MANAGER,
            CASHIER,
            SELLER,
            ADMIN
        }

    public String getFirstName() {
        return firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getFullName() {
        return firstName + " " + familyName;
    }

    public Role getRole() {
        return role;
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

    public Branch getBranch() {
        if (branch == null && branchText != null && !branchText.isEmpty()) {
            branch = new Branch(branchText);
        }
        return branch;
    }



}

