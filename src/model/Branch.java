package model;

import java.io.Serializable;
import main.Servers;

public class Branch implements Serializable{
    private String name;
    private int id;
    private boolean isConnected;
    
   // private static List<Socket> branchClients;


  

    public Branch(String name, int id, boolean isConnected) {
        this.name = name;
        this.id = id;
        this.setConnectionStatus(isConnected);
    }

     public Branch(String name) {
        this.name = name;
        this.id = hashIdFromName(name);
        this.isConnected = false;
    }

    private void setConnectionStatus(boolean isConnected) {
       if (isConnected) {
           System.out.println("Branch " + name + " is now connected.");
           Servers.connectedBranches.add(this);
           this.isConnected = true;
       } else {

            if(!Servers.connectedBranches.contains(this)){return;}

            Servers.connectedBranches.remove(this);
            this.isConnected = false;
           System.out.println("Branch " + name + " is now disconnected.");
       }
       System.out.println("Currently connected branches: " + Servers.connectedBranches.size());
    }

    public String getName() {
        return name;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connectionStatus) {
        this.isConnected = connectionStatus;
    }

    public int getId() {
        return id;
    }

    public static int hashIdFromName(String name) {
    return Math.abs(name.hashCode());
}

    public String getEmployeeFilePath() {
        return "employee" + "_" + getName() + "_" + getId() + ".ser";
    }

    public String getCustomerFilePath() {
        return "customer" + "_" + getName() + "_" + getId() + ".ser";
    }

    
}