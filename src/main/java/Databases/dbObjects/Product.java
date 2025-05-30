package Databases.dbObjects;

public class Product {
    private String name;
    private int categoryId;
    private double price;
    private int quantity;

    public Product(String name, int categoryId, double price, int quantityInStock) {
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.quantity = quantityInStock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantityInStock() {
        return quantity;
    }

    public void setQuantityInStock(int quantityInStock) {
            this.quantity = quantityInStock;
    }
}