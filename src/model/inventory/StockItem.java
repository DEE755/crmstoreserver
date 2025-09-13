package model.inventory;

public class StockItem implements java.io.Serializable {

    private String name;

    private int quantity;
    private double price;
    private int id;
    private Category category;
    

    public StockItem(String name, int quantity, double price, Category category) {
        this.name = name;
        this.id=generateItemId();
        this.quantity = quantity;
        this.price = price;
        this.category = category;
    }

    //FOR EXISTING ITEMS
    public StockItem(String name, int id, int quantity, double price, Category category) {
        this.name = name;
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
    }

    
     public enum Category {
        ELECTRONICS, CLOTHING, GROCERY, MISC
    }

   

    public String getName() {
        return name;
    }


    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    int generateItemId() {
        return (int) (Math.random() * 100000);
    }


    public Enum<Category> getCategory() {
        return category;
    }

}
