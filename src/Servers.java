import java.io.*;
import java.net.*;

public class Servers {

    private static ServerSocket serverSocket = null;

    public static void main(String[] args) throws IOException {

        /*EmployeeSerializerTool employeeSerializerTool = new EmployeeSerializerTool();
        
        try {
            employeeSerializerTool.saveEmployee(new Employee(0, "administrator", "admin@admin.com", "admin", "admin", "00000000"), "employees.ser");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error saving employee: " + e.getMessage());
        } */

        try {
            serverSocket = new ServerSocket(1234);
            System.out.println("Server is listening on port 1234...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected! from " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                // hand off each client to its own thread
                new Thread(new ClientHandler(clientSocket)).start();
            }

        } catch (IOException e) {
            System.err.println(e);
        } finally {
            if (serverSocket != null) try { serverSocket.close(); } catch (IOException ignored) {}
        }
    }


    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            DataInputStream inputStream = null;
            PrintStream outputStream = null;
            String line = "";
            Commands commands = new Commands(clientSocket);

            try {
                inputStream = new DataInputStream(clientSocket.getInputStream());
                outputStream = new PrintStream(clientSocket.getOutputStream());

                outputStream.println("YOU ARE NOW CONNECTED");

                while (!"exit".equals(line)) {
                    line = inputStream.readLine();
                    if (line == null) break; // client closed
                    //outputStream.println("Echo: " + line);
                    System.out.println("[" + clientSocket.getPort() + "] Received: " + line);
                    commands.handleCommand(line);
                }

            } catch (IOException e) {
                System.err.println("Client " + clientSocket + " error: " + e);
            } finally {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) try { inputStream.close(); } catch (IOException ignored) {}
                if (clientSocket != null) try { clientSocket.close(); } catch (IOException ignored) {}
            }
        }

        
    }
}