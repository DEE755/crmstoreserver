package model;

import java.io.Serializable;
import java.util.Set;
import main.Servers;

public class Branch implements Serializable{
    private String name;
    private int id;
    private boolean isConnected;
    private static Set<Branch> existingBranches = new java.util.HashSet<>();

    private Employee connectedEmployee;
   // private static List<Socket> branchClients;


  
   

    public Branch(String name, int id, boolean isConnected) {
        this.name = name;
        this.id = id;
        this.setConnectionStatus(isConnected);
        existingBranches.add(this);
    }

    public Branch(String name, int id, boolean isConnected, Employee connectedEmployee) {
        this.name = name;
        this.id = id;
        this.setConnectionStatus(isConnected);
        this.connectedEmployee = connectedEmployee;
        existingBranches.add(this);
    }

     public Branch(String name) {
        this.name = name;
        this.id = hashIdFromName(name);
        this.isConnected = false;
        existingBranches.add(this);
    }

    

    public void setConnectionStatus(boolean isConnected) {
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

    public String getInventoryFilePath() {
        return "inventory" + "_" + getName() + "_" + getId() + ".ser";
    }

    public Employee getConnectedEmployee() {
        if (isConnected()) {
            return connectedEmployee;
        } else {
            System.out.println("No one is connected at Branch " + name + ".");
            return null;
        }
    }

    public void setConnectedEmployee(Employee employee) {
        if (!isConnected()) {
            System.out.println("Cannot set connected employee. Branch " + name + " is not connected.");
            return;
        }
        this.connectedEmployee = employee;
    }
    

    public static void detectExistingBranches() {
        java.io.File srcDir = new java.io.File(".");
        java.io.FilenameFilter filter = (dir, name) -> name.startsWith("employee_") && name.endsWith(".ser");
        String[] files = srcDir.list(filter);

        if (files != null) {
            for (String fileName : files) {
                // Format: employee_name_id.ser
                String[] parts = fileName.substring(0, fileName.length() - 4).split("_");
                if (parts.length == 3) {
                    String branchName = parts[1];
                    int branchId;
                    try {
                        branchId = Integer.parseInt(parts[2]);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    
                    Branch branch = new Branch(branchName, branchId, false);
                    existingBranches.add(branch);
                    System.out.println("Detected existing branch: Name=" + branchName + ", ID=" + branchId);
                }
            }
        }
    }

    public String getStockItemFilePath() {
        return "stockItems" + "_" + getName() + "_" + getId() + ".ser";
    }
}