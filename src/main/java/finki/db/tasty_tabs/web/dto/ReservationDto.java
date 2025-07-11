package finki.db.tasty_tabs.web.dto;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class ReservationDto {
    private Long id;
    private Integer stayLength;
    private LocalDateTime datetime;
    private LocalDateTime creationTimestamp;
    private Integer numberOfPeople;
    private Long userId;
}