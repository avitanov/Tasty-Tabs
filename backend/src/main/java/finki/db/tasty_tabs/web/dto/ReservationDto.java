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
        String status // "ACCEPTED" | "PENDING"
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
                "PENDING"
        );
    }

    // new factory with accepted flag
    public static ReservationDto from(Reservation r, boolean accepted) {
        return new ReservationDto(
                r.getId(),
                r.getStayLength(),
                r.getDatetime(),
                r.getCreationTimestamp(),
                r.getNumberOfPeople(),
                r.getUser().getEmail(),
                accepted ? "ACCEPTED" : "PENDING"
        );
    }
}