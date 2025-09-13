package model.inventory;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<StockItem> items;

    public Inventory() {
        items = new ArrayList<>();
    }

    public void addItem(StockItem item) {
        items.add(item);
    }

    public void removeItem(StockItem item) {
        items.remove(item);
    }

    public List<StockItem> getItems() {
        return items;
    }
}
