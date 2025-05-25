package Databases.dbObjects;

public class SaleItem {
    private int saleItemID;
    private int saleID;
    private int productID;
    private int quantity;
    private java.math.BigDecimal unitPrice;

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }
    public int getSaleID() {
        return saleID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }
    public int getProductID() {
        return productID;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setUnitPrice(java.math.BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    public java.math.BigDecimal getUnitPrice() {
        return unitPrice;
    }
    public void setSaleItemID(int saleItemID) {
        this.saleItemID = saleItemID;
    }
    public int getSaleItemID() {
        return saleItemID;
    }

    // Constructors, Getters, Setters
}
