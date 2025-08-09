package finki.db.tasty_tabs.web.dto;
import finki.db.tasty_tabs.entity.Manager;
import finki.db.tasty_tabs.entity.Shift;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
public record CreateShiftDto(
        LocalDate date,
        LocalDateTime start,
        LocalDateTime end
) {
    public Shift toShift(Manager manager) {
        return new Shift(
                date,
                start,
                end
        );
    }
}