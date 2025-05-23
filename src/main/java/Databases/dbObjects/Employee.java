package Databases.dbObjects;
import java.util.Date;
public  class Employee {
    private String firstName;
    private String lastName;
    private int roleId; // 1 for Admin, 2 for Cashier, etc.

    public Employee(String firstName, String lastName, int roleId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleId = roleId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getRoleId() {
        return roleId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", roleId=" + roleId +
                '}';
    }
}
