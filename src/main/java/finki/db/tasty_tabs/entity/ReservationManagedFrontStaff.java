package finki.db.tasty_tabs.entity;// finki.db.tasty_tabs.entity.ReservationManagedFrontStaff
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import finki.db.tasty_tabs.entity.composite_keys.ReservationManagedFrontStaffId;

@Entity
@Table(name = "frontstaff_managed_reservations")
@Data
@NoArgsConstructor
public class ReservationManagedFrontStaff {

    @EmbeddedId
    private ReservationManagedFrontStaffId id;

    @ManyToOne
    @MapsId("reservationId")
    @JoinColumn(name = "reservation_id", referencedColumnName = "id")
    private Reservation reservation;

    @ManyToOne
    @MapsId("frontstaffId")
    @JoinColumn(name = "front_staff_id", referencedColumnName = "employee_id")
    private FrontStaff frontStaff;

    @ManyToOne
    @MapsId("tableNumber")
    @JoinColumn(name = "table_number", referencedColumnName = "table_number")
    private RestaurantTable restaurantTable;
}