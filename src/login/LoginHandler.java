package login;

import java.util.List;
import model.Employee;

public class LoginHandler implements LoginHandlerInterface {
    private boolean loggedIn = false;

   

    @Override
   public Employee authenticate(String enteredUsername, String enteredPassword, List<Employee> employees) 
    
    {     
                for (Employee employee : employees) {
                    System.out.println("Checking employee: " + employee.getUsername() + " with password: " + employee.getPassword());
            if (employee.getUsername().equals(enteredUsername) && 
                employee.getPassword().equals(enteredPassword)) {
                loggedIn = true;
                System.out.println("Employee Found: " + employee.getFullName());
                return employee;
            }
        }

        return null;
    }
    


    @Override
    public boolean isLoggedIn() {
        return loggedIn;
    }

     public void setLoggedIn(boolean logged) {
        this.loggedIn=logged;
    }
}



   