package Databases.dbObjects;
import java.util.Date;
public class Shift {
    private int shiftID;
    private int employeeID;
    private int supermarketID;
    private  Date startTime;
    private Date endTime;

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
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Date getStartTime() {
        return startTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public Date getEndTime() {
        return endTime;
    }
    public void setShiftID(int shiftID) {
        this.shiftID = shiftID;
    }
    public int getShiftID() {
        return shiftID;
    }

    // Constructors, Getters, Setters
}
