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

    @OneToMany(mappedBy = "frontStaff")
    private List<ReservationManagedFrontStaff> managedReservations;

    public Double getTipPercent() {
        return tipPercent;
    }

    public void setTipPercent(Double tipPercent) {
        this.tipPercent = tipPercent;
    }

    public StaffRole getStaffRole() {
        return staffRole;
    }

    public void setStaffRole(StaffRole staffRole) {
        this.staffRole = staffRole;
    }

    public List<TabOrder> getTabOrders() {
        return tabOrders;
    }

    public void setTabOrders(List<TabOrder> tabOrders) {
        this.tabOrders = tabOrders;
    }

    public List<ReservationManagedFrontStaff> getManagedReservations() {
        return managedReservations;
    }

    public void setManagedReservations(List<ReservationManagedFrontStaff> managedReservations) {
        this.managedReservations = managedReservations;
    }
}
