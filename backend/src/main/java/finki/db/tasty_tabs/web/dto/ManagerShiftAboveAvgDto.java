package finki.db.tasty_tabs.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record ManagerShiftAboveAvgDto(
        String period,               // YYYY-MM
        Long shiftId,
        LocalDate shiftDate,
        LocalTime shiftStartTime,
        LocalTime shiftEndTime,
        String managerEmail,
        BigDecimal shiftRevenue,
        BigDecimal avgRevenuePerShift,
        BigDecimal aboveBy
) {}