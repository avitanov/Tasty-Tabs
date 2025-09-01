package finki.db.tasty_tabs.web.dto;
import finki.db.tasty_tabs.entity.Reservation;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
public record ReservationDto(
        Long id,
        Integer stayLength,
        LocalDateTime datetime,
        LocalDateTime creationTimestamp,
        Integer numberOfPeople,
        String email
) {
    public static ReservationDto from(Reservation reservation) {
        return new ReservationDto(
                reservation.getId(),
                reservation.getStayLength(),
                reservation.getDatetime(),
                reservation.getCreationTimestamp(),
                reservation.getNumberOfPeople(),
                reservation.getUser().getEmail()
        );
    }
}