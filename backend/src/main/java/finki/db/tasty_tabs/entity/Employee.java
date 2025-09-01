package finki.db.tasty_tabs.entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entity: Employee
 * Description: Represents employees of the system. Inherits from User.
 */
@Entity
@Table(name = "employees")
@Data
@PrimaryKeyJoinColumn(name = "user_id") // Links to the User table
public class Employee extends User {

    @Column(name = "net_salary", nullable = false)
    private Double netSalary;

    @Column(name = "gross_salary", nullable = false)
    private Double grossSalary;

    // Relationship for assignments given to this employee
    @OneToMany(mappedBy = "employee")
    private List<Assignment> assignments;

    public Employee() {
        super();
    }
    public Employee(Double netSalary, Double grossSalary) {
        super();
        this.netSalary = netSalary;
        this.grossSalary = grossSalary;
    }

    public Employee(String email, String street, String city, String phoneNumber, Double netSalary, Double grossSalary, List<Assignment> assignments) {
        super(email, street, city, phoneNumber);
        this.netSalary = netSalary;
        this.grossSalary = grossSalary;
        this.assignments = assignments;
    }

    public Double getNetSalary() {
        return netSalary;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public void setNetSalary(Double netSalary) {
        this.netSalary = netSalary;
    }

    public Double getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(Double grossSalary) {
        this.grossSalary = grossSalary;
    }

    public UserType getUserType(){
        return UserType.EMPLOYEE;
    }
}
