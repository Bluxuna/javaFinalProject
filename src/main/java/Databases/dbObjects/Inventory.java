package Databases.dbObjects;
import java.util.Date;
public class Inventory {
    private int inventoryID;
    private int productID;
    private int supermarketID;
    private int quantity;
    private Date lastUpdated;

    public int getInventoryID() {
        return inventoryID;
    }
    public int getProductID() {
        return productID;
    }
    public int getSupermarketID() {
        return supermarketID;
    }
    public int getQuantity() {
        return quantity;
    }
    public Date getLastUpdated() {
        return lastUpdated;
    }
    public void setInventoryID(int inventoryID) {
        this.inventoryID = inventoryID;
    }
    public void setProductID(int productID) {
        this.productID = productID;
    }
    public void setSupermarketID(int supermarketID) {
        this.supermarketID = supermarketID;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


    // Constructors, Getters, Setters
}

