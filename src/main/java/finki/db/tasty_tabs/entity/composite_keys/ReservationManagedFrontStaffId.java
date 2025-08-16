package finki.db.tasty_tabs.entity.composite_keys;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
public class ReservationManagedFrontStaffId implements Serializable {

    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "front_staff_id")
    private Long frontstaffId;

    @Column(name = "table_number")
    private Long tableNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationManagedFrontStaffId that = (ReservationManagedFrontStaffId) o;
        return Objects.equals(reservationId, that.reservationId) &&
                Objects.equals(frontstaffId, that.frontstaffId) &&
                Objects.equals(tableNumber, that.tableNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId, frontstaffId, tableNumber);
    }
}