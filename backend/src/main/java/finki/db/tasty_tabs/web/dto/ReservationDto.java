package finki.db.tasty_tabs.web.dto;
import finki.db.tasty_tabs.entity.Reservation;
import java.time.LocalDateTime;
public record ReservationDto(
        Long id,
        Integer stayLength,
        LocalDateTime datetime,
        LocalDateTime creationTimestamp,
        Integer numberOfPeople,
        String email,
        String status, // "ACCEPTED" | "PENDING"
        Long assignedTableNumber,     // null if not accepted
        String frontStaffName         // null if not accepted; using email as fallback
) {
    // legacy factory (defaults to PENDING)
    public static ReservationDto from(Reservation r) {
        return new ReservationDto(
                r.getId(),
                r.getStayLength(),
                r.getDatetime(),
                r.getCreationTimestamp(),
                r.getNumberOfPeople(),
                r.getUser().getEmail(),
                "PENDING",
                null,
                null
        );
    }

    // new factory with accepted flag
    public static ReservationDto from(Reservation r, boolean accepted, Long tableNumber, String frontStaffName) {
        return new ReservationDto(
                r.getId(),
                r.getStayLength(),
                r.getDatetime(),
                r.getCreationTimestamp(),
                r.getNumberOfPeople(),
                r.getUser().getEmail(),
                accepted ? "ACCEPTED" : "PENDING",
                accepted ? tableNumber : null,
                accepted ? frontStaffName : null
        );
    }
}