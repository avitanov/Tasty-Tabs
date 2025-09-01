package finki.db.tasty_tabs.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyOpsDto(
        LocalDate operationDate,
        Long totalReservations,
        Long totalOrders,
        Long uniqueCustomers,
        Long activeEmployees,
        BigDecimal dailyRevenue,
        Boolean meetsConversionTarget,
        String revenueCategory
) {}
