package finki.db.tasty_tabs.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ManagerShiftAboveAvgDto(
        String period,               // YYYY-MM
        Long shiftId,
        LocalDate shiftDate,
        String managerEmail,
        BigDecimal shiftRevenue,
        BigDecimal avgRevenuePerShift,
        BigDecimal aboveBy
) {}
