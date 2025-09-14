package main;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import model.Branch;
import serialization.CustomerSerializer;
import serialization.EmployeeSerializer;
import serialization.Logger;
import serialization.StockItemSerializer;

public class Servers {
    private static ServerSocket serverSocket = null;

    public static Set<Branch> connectedBranches = new HashSet<>();
    public static List<ClientHandler> clientHandlers = new ArrayList<>();

    public static final ThreadLocal<Branch> currentBranch = new ThreadLocal<>();
    private static Logger logger = Logger.getInstance();

    public static void main(String[] args) {

            //detect existing branches
            Branch.detectExistingBranches();
            // Start server socket
            System.out.println("Starting server...");
            //default port 1234

        try {
            serverSocket = new ServerSocket(1234);
            System.out.println("Server is listening on port 1234...");

            while (true) {
                Socket clientSocket = serverSocket.accept();;//blocking next call until a client connects
                //When a Client connects:

                System.out.println("Client connected! from " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                // each client gets its own thread
                new Thread(new ClientHandler(clientSocket)).start();
            }

        } catch (IOException e) {
            System.err.println(e);
        } finally {
            if (serverSocket != null) try { serverSocket.close(); } catch (IOException ignored) {}
        }
    }


    static class ClientHandler implements Runnable {
        final Socket clientSocket;
        Branch branch;

        ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            clientHandlers.add(this);
        }

        @Override
        public void run() {
            DataInputStream inputStream = null;
            PrintStream outputStream = null;
            String line = "";
            Commands commands = new Commands(clientSocket);

            try {
                //initialize input and output streams for the specific client
                inputStream = new DataInputStream(clientSocket.getInputStream());
                outputStream = new PrintStream(clientSocket.getOutputStream());

                outputStream.println("YOU ARE NOW CONNECTED");
                line = inputStream.readLine();

                System.out.println(line);

                String[] parts= line.split(" ");
                Branch newlyConnectedBranch = new Branch(parts[0], Integer.parseInt(parts[1]), true);
                this.branch = newlyConnectedBranch; //assign branch to this client handler
                Servers.currentBranch.set(this.branch);
                System.out.println("Branch created and added: " + newlyConnectedBranch.getName() + " with ID " + newlyConnectedBranch.getId() + " connected: " + newlyConnectedBranch.isConnected());
                Branch.addBranchAndUpdateConnectionNoDuplicates(newlyConnectedBranch);
                commands.refreshAssociatedBranch(newlyConnectedBranch);
                logger.log(" Branch connected: " + newlyConnectedBranch.getName() + " at port " + clientSocket.getPort());

                
                //INITIALIZE SERIALIZERS FOR THE BRANCH
                CustomerSerializer customerSerializer = CustomerSerializer.getInstance();
                EmployeeSerializer employeeSerializer = EmployeeSerializer.getInstance();
                StockItemSerializer stockItemSerializer = StockItemSerializer.getInstance();

                
                //checking if branch has employees data in the server and creates admin employee if not
                // Check if the employee file exists
                 util.Utility.createEmployeeFileIfNotExists();

                //MAIN LOOP - keep listening for commands from the client
                while (!"exit".equals(line)) {
                    line = inputStream.readLine();
                    if (line == null) break; // client closed
                    //outputStream.println("Echo: " + line);
                    System.out.println("[" + clientSocket.getPort() + "] Received: " + line);
                    commands.handleCommand(line);
                }

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Client " + clientSocket + " error: " + e);
            } finally {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) try { inputStream.close(); } catch (IOException ignored) {}
                if (clientSocket != null) try { clientSocket.close(); } catch (IOException ignored) {}
                Servers.currentBranch.remove();
            }
        }

        
    }
}