package Databases.dbObjects;

public class Product {
    private int productId;
    private String name;
    private int categoryId;
    private double price;
    private int quantity;
    private String barcode;

    public Product(String name, int categoryId, double price, int quantityInStock) {
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.quantity = quantityInStock;
        this.barcode = "";
    }

    public Product(String name, int categoryId, double price, int quantityInStock, String barcode) {
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.quantity = quantityInStock;
        this.barcode = barcode;
    }

    public Product(int productId, String name, int categoryId, double price, int quantityInStock) {
        this.productId = productId;
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.quantity = quantityInStock;
        this.barcode = "";
    }

    public Product(int productId, String name, int categoryId, double price, int quantityInStock, String barcode) {
        this.productId = productId;
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.quantity = quantityInStock;
        this.barcode = barcode;
    }
    public String getBarcode() {
        return barcode;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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
