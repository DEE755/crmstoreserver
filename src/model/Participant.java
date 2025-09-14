package model;

public class Participant implements java.io.Serializable {

    protected String firstName;
    protected String familyName;
    protected int id;
    protected String phoneNumber;
    protected String email;

    public Participant(String firstName, String familyName, String email, int id, String phoneNumber) {
        this.firstName = firstName;
        this.familyName = familyName;
        this.email = email;
        this.id = id;
        this.phoneNumber = phoneNumber;
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

    public int getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    

}
