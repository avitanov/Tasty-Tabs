package finki.db.tasty_tabs.entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entity: StaffRole
 * Description: Defines roles for staff members.
 */
@Entity
@Table(name = "staff_roles")
@Data
@NoArgsConstructor
public class StaffRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "staffRole")
    private List<FrontStaff> frontStaffs;

    @OneToMany(mappedBy = "staffRole")
    private List<BackStaff> backStaffs;
}