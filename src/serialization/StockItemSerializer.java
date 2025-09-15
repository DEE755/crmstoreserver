package serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import main.Servers;
import model.Branch;
import model.inventory.StockItem;

public class StockItemSerializer extends CustomSerializer {

      private String stockItemDataFile;
    private Branch branch;

    private static final ThreadLocal<StockItemSerializer> instance =
        ThreadLocal.withInitial(() -> {
            Branch branch = Servers.currentHandler.get().getBranchClientHandler();
            return new StockItemSerializer(branch);
        });

    // Call this method to get the instance for the current thread and linking late init branch
    public static StockItemSerializer getInstance() {
    StockItemSerializer serializer = instance.get();
    Branch currentBranch = Servers.currentHandler.get().getBranchClientHandler();
    //linking late init branch if not set yet
    if (serializer.branch == null && currentBranch != null) {
        serializer.branch = currentBranch;
        serializer.setFilesPaths();
    }
    return serializer;
}

//According to present code, when calling this constructor branch is always null. Udpate it in getInstance after getting currentBranch from Servers
    private StockItemSerializer(Branch branch) {
        this.branch = branch;
        setFilesPaths();
    }


    private void setFilesPaths() {
       if (branch != null) {
            this.stockItemDataFile = branch.getStockItemFilePath();
            System.out.println("Current branch in StockItemSerializer: " + branch.getName());
        } else {
            this.stockItemDataFile = util.Constants.STOCK_ITEM_DATA_FILE;
            System.out.println("No current branch set in StockItemSerializer.");
        }
    }

    public void saveStockItem(StockItem stockItem) throws IOException, ClassNotFoundException {
        List<StockItem> stockItems;
        // Try to load existing stock items, or create a new list if file doesn't exist
        try {
            stockItems = loadStockItemList();
        } catch (IOException | ClassNotFoundException e) {
            stockItems = new java.util.ArrayList<>();
        }
        stockItems.add(stockItem);
        saveStockItemList(stockItems);
    }

    public StockItem loadStockItem(int stockItemId) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("stockItem_" + stockItemId + ".ser"))) {
            return (StockItem) in.readObject();
        }
    }

    public StockItem loadStockItemById(int stockItemId) throws IOException, ClassNotFoundException {
        List<StockItem> stockItems = loadStockItemList();
        for (StockItem item : stockItems) {
            if (item.getId() == stockItemId) {
                return item;
            }
        }
        throw new IOException("StockItem with ID " + stockItemId + " not found.");
    }

    public void deleteStockItem(int stockItemId) throws IOException, ClassNotFoundException {
       try {
           List<StockItem> stockItems = loadStockItemList();
           stockItems.removeIf(s -> s.getId() == stockItemId);
           saveStockItemList(stockItems);
       } catch (IOException e) {
           System.err.println("Error loading stock item list: " + e.getMessage());
       }
    }

    public void saveStockItemList(List<StockItem> stockItems) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(stockItemDataFile))) {
            out.writeObject(stockItems);
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<StockItem> loadStockItemList() throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(stockItemDataFile))) {
            return (List<StockItem>) in.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            // If the file does not exist or is empty, return an empty list
            System.err.println("There is no stock item data available. Please add stock items first.");
            return new java.util.ArrayList<>();
        }
    }


public void modifyStockItemQuantity(int stockItemId, int newQuantity) throws IOException, ClassNotFoundException {
    List<StockItem> stockItems = loadStockItemList();
    for (StockItem item : stockItems) {
        if (item.getId() == stockItemId) {
            item.setQuantity(newQuantity);
            System.out.println("Modified StockItem: " + item);
            break;
        }
    }
    saveStockItemList(stockItems); 
    
}

}


