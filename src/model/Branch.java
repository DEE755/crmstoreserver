package model;

import java.net.Socket;
import java.util.List;

public class Branch {
    private String name;
    private int id;
    private boolean isConnected;
    private static List<Socket> branchClients;


    public Branch(String name, int id, boolean isConnected, Socket clientSocket) {
        this.name = name;
        this.id = id;
        this.isConnected = isConnected;

        if (branchClients == null) {
            branchClients = new java.util.ArrayList<>();
        }
        branchClients.add(clientSocket);
    }

    public String getName() {
        return name;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public int getId() {
        return id;
    }
}