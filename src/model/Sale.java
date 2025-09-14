package model;

import java.util.Scanner;
import model.customer.Customer;
import model.inventory.StockItem;

public class Sale {
    Customer buyer;
    StockItem itemSold;
    Employee seller;
    double salePrice;
    int quantitySold;
    double totalPrice;
    int saleId;
    java.time.LocalDate saleDate;
    Scanner scanner = new Scanner(System.in);

   

public Sale(Customer buyer, StockItem itemSold, Employee seller, double salePrice, int quantitySold){
    this.buyer=buyer;
    this.itemSold=itemSold;
    this.seller=seller;
    this.salePrice=salePrice;
    this.quantitySold=quantitySold;
    this.totalPrice=salePrice*quantitySold;
    this.saleDate=java.time.LocalDate.now();
}



private static double calculateTotalPrice(Customer buyer, StockItem itemSold, int quantitySold, int otherDiscount)
{
    return itemSold.getPrice() * quantitySold * buyer.getDiscount();
}


public String getTotalPrice() {
    return String.valueOf(this.totalPrice);
}


public String getQuantitySold() {
    return String.valueOf(this.quantitySold);
}


public StockItem getItemSold() {
    return this.itemSold;
}


public Customer getBuyer() {
    return this.buyer;
}

public Employee getSeller() {
    return this.seller;
      
}
}
