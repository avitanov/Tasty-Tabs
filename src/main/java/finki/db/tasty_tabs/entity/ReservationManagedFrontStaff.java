package finki.db.tasty_tabs.entity;

import finki.db.tasty_tabs.entity.composite_keys.ReservationManagedFrontStaffId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation_managed_frontstaff")
@Data
@NoArgsConstructor
@IdClass(ReservationManagedFrontStaffId.class)
public class ReservationManagedFrontStaff {

    @Id
    @ManyToOne
    @JoinColumn(name = "reservation_id", referencedColumnName = "id")
    private Reservation reservation;

    @Id
    @ManyToOne
    @JoinColumn(name = "frontstaff_id", referencedColumnName = "employee_id")
    private FrontStaff frontStaff;

    @Id
    @ManyToOne
    @JoinColumn(name = "table_number", referencedColumnName = "table_number")
    private RestaurantTable restaurantTable;
}
