package Databases.dbObjects;
import java.util.Date;
public class Employee {
    private int employeeId;
    private String firstName;
    private String lastName;
    private int roleId; // 1 for Admin, 2 for Cashier, etc.
    private int supermarketId;
    private String email;
    private String passwordHash;
    private Date hireDate;


    public Employee(String firstName, String lastName, int roleId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleId = roleId;
    }

    public Employee(int employeeId, String firstName, String lastName, int roleId, int supermarketId) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleId = roleId;
        this.supermarketId = supermarketId;
    }

    public Employee(int employeeId, String firstName, String lastName, int roleId, int supermarketId, 
                   String email, String passwordHash, Date hireDate) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleId = roleId;
        this.supermarketId = supermarketId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.hireDate = hireDate;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getSupermarketId() {
        return supermarketId;
    }

    public void setSupermarketId(int supermarketId) {
        this.supermarketId = supermarketId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", roleId=" + roleId +
                ", supermarketId=" + supermarketId +
                ", email='" + email + '\'' +
                ", hireDate=" + hireDate +
                '}';
    }
}
