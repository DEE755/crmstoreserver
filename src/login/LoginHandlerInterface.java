package login;

import java.util.List;
import model.Employee;

public interface LoginHandlerInterface {
    Employee authenticate(String enteredUsername, String enteredPassword, List<Employee> employees);
    boolean isLoggedIn();
}
