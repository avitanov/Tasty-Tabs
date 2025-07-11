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
@Table(name = "employee")
@Data
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id") // Links to the User table
public class Employee extends User {

    @Column(name = "net_salary", nullable = false)
    private Double netSalary;

    @Column(name = "gross_salary", nullable = false)
    private Double grossSalary;

    // Relationship for assignments given to this employee
    @OneToMany(mappedBy = "employee")
    private List<Assignment> assignments;
}
