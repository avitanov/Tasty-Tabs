package finki.db.tasty_tabs.web.dto;
import lombok.Data;
import java.time.LocalDateTime;

public record CreateReservationDto(
        Integer stayLength,
        LocalDateTime datetime,
        Integer numberOfPeople
) {
}