package finki.db.tasty_tabs.entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entity: Manager
 * Description: A disjoint specialization of the Employee entity for managers.
 */
@Entity
@Table(name = "manager")
@Data
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "employee_id")
public class Manager extends Employee {

    // Relationship for shifts created by this manager
    @OneToMany(mappedBy = "manager")
    private List<Shift> createdShifts;

    // Relationship for assignments created by this manager
    @OneToMany(mappedBy = "manager")
    private List<Assignment> createdAssignments;
}
