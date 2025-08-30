import java.io.*;
import java.net.*;
import model.Employee;




public class Server {

     private static ServerSocket serverSocket = null;
     private static Socket clientSocket = null;
     private static DataInputStream inputStream = null;
     private static PrintStream outputStream = null;
     private static String line = "";
     private static ObjectOutputStream outputObject = null;
    public static void main(String[] args) throws IOException {

    try {
        serverSocket = new ServerSocket(1234);
        System.out.println("Server is listening on port 1234...");
        clientSocket = serverSocket.accept();
        System.out.println("Client connected! from " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());


       inputStream= new DataInputStream(clientSocket.getInputStream());
       outputStream = new PrintStream(clientSocket.getOutputStream());
    

        outputStream.println("YOU ARE NOW CONNECTED");
        
        // Example: Echo received data
    
        while (!line.equals("exit")) {
            line=inputStream.readLine();
            outputStream.println("Echo: " + line);
            System.out.println( "Received: " + line);

        }

        
    }

    catch (IOException e) {
        System.err.println(e);

    }

    finally
    {
        inputStream.close();
        outputStream.close();
        clientSocket.close();
        serverSocket.close();
    }

    }

    
    public ObjectOutputStream sendEmployee(Employee employee) {
        try {
            ObjectOutputStream outputObject = new ObjectOutputStream(this.clientSocket.getOutputStream());
            outputObject.writeObject(employee);
            return outputObject;
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}