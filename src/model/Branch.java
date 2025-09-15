package model;

import java.io.Serializable;
import java.util.List;
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
        addBranchAndUpdateConnectionNoDuplicates(this);
    }

    public Branch(String name, int id, boolean isConnected, Employee connectedEmployee) {
        this.name = name;
        this.id = id;
        this.setConnectionStatus(isConnected);
        this.connectedEmployee = connectedEmployee;
        addBranchAndUpdateConnectionNoDuplicates(this);
    }

     public Branch(String name) {
        this.name = name;
        this.id = hashIdFromName(name);
        this.isConnected = false;
        addBranchAndUpdateConnectionNoDuplicates(this);
    }

    

    public void setConnectionStatus(boolean isConnected) {
       if (isConnected) {
           System.out.println("Branch " + name + " is now connected.");
           addBranchAndUpdateConnectionNoDuplicates(this);

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
        return util.Constants.EMPLOYEE_FILE;
        //return "employee" + "_" + getName() + "_" + getId() + ".ser";
    }

    public String getCustomerFilePath() {
            return util.Constants.CUSTOMER_DATA_FILE;
        //return "customer" + "_" + getName() + "_" + getId() + ".ser";
    }


    public String getStockItemFilePath() {
        return util.Constants.STOCK_ITEM_DATA_FILE;
        //return "stockItems" + "_" + getName() + "_" + getId() + ".ser";
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

        if (!isConnected() || employee == null) {
            System.out.println("Cannot set connected employee. Branch " + name + " is not connected or employee is null.");
            return;
        }
        System.out.println("Setting connected employee for Branch " + name + " to " + employee.getFullName());
        this.connectedEmployee = employee;
        addBranchAndUpdateConnectionNoDuplicates(this);
    }
    

    public static Set<Branch> detectExistingBranches() {
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

                    //search of the branch in the currently connected branches to copy the connected employee if any
                   Branch branch = Branch.findBranchById(branchId,Servers.connectedBranches);

                   //System.out.println("FOUND BRANCH BY ID "+branchId+" in connected branches: "+ (branch != null) + " name: "+(branch != null ? branch.getConnectedEmployee().getFullName() : "NONE"));

                    if (branch == null) {
                        System.out.println("Creating new branch since null: Name=" + branchName + ", ID=" + branchId);
                        branch = new Branch(branchName, branchId, false, null);
                    }


                    existingBranches.add(branch);
                    
                }
            }
        }
                                    return existingBranches;
    }

    



    public static Branch findBranchById(int id, List<Branch> branches) {
        for (Branch bra : branches) {
            if (bra.getId() == id) {
                return bra;
            }
        }
        return null;
    }


    public static Branch findBranchById(int id, Set<Branch> branches) {
        for (Branch bra : branches) {
            if (bra.getId() == id) {
                return bra;
            }
        }
        return null;
    }

    public static void addBranchAndUpdateConnectionNoDuplicates(Branch branchToAdd) 
    {
        Branch existingBranch = findBranchById(branchToAdd.getId(), Servers.connectedBranches);
        if (existingBranch != null) {
            Servers.connectedBranches.remove(existingBranch);
        }
        Servers.connectedBranches.add(branchToAdd);

        Branch existingBranch2 = findBranchById(branchToAdd.getId(), existingBranches);
        if (existingBranch2 != null) {
            existingBranches.remove(existingBranch2);
        }
        existingBranches.add(branchToAdd);
    }
}