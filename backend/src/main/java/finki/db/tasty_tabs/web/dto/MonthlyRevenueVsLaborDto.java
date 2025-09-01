package finki.db.tasty_tabs.web.dto;

import java.math.BigDecimal;

public record MonthlyRevenueVsLaborDto(
        String period,                // YYYY-MM
        BigDecimal totalRevenue,
        BigDecimal totalLaborCost,
        BigDecimal laborAsPercentOfRevenue
) {}
