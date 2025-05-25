package Databases.dbObjects;
import java.sql.Timestamp;
public class Sale {
    private int saleID;
    private int supermarketID;
    private int employeeID;
    private Timestamp saleDate;
    private double totalAmount;

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }
    public int getSaleID() {
        return saleID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }
    public int getEmployeeID() {
        return employeeID;
    }
    public void setSupermarketID(int supermarketID) {
        this.supermarketID = supermarketID;
    }
    public int getSupermarketID() {
        return supermarketID;
    }
    public void setSaleDate(Timestamp saleDate) {
        this.saleDate = saleDate;
    }
    public Timestamp getSaleDate() {
        return saleDate;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public double getTotalAmount() {
        return totalAmount;
    }


    // Constructors, Getters, Setters
}

