package finki.db.tasty_tabs.entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity: Shift
 * Description: Represents a work shift in the system.
 */
@Entity
@Table(name = "shift")
@Data
@NoArgsConstructor
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "employee_id", nullable = false)
    private Manager manager;

    @OneToMany(mappedBy = "shift")
    private List<Assignment> assignments;
}
