package finki.db.tasty_tabs.entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entity: FrontStaff
 * Description: A disjoint specialization of the Employee entity for front-facing staff members.
 */
@Entity
@Table(name = "front_staff")
@Data
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "employee_id")
public class FrontStaff extends Employee {

    @Column(name = "tip_percent")
    private Double tipPercent;

    @ManyToOne
    @JoinColumn(name = "staff_role_id", referencedColumnName = "id")
    private StaffRole staffRole;

    // Relationship for tab orders handled by this front staff member
    @OneToMany(mappedBy = "frontStaff")
    private List<TabOrder> tabOrders;
}
