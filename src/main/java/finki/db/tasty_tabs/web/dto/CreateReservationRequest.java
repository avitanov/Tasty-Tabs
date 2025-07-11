package finki.db.tasty_tabs.web.dto;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class CreateReservationRequest {
    private Long customerId;
    private Integer stayLength;
    private LocalDateTime datetime;
    private Integer numberOfPeople;
}