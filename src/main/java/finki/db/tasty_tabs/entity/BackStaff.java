package finki.db.tasty_tabs.entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Entity: BackStaff
 * Description: A disjoint specialization of the Employee entity for back-office staff members.
 */
@Entity
@Table(name = "back_staff")
@Data
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "employee_id")
public class BackStaff extends Employee {

    @ManyToOne
    @JoinColumn(name = "staff_role_id", referencedColumnName = "id")
    private StaffRole staffRole;
}
