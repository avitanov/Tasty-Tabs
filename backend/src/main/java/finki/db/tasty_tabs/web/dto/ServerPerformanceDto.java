package finki.db.tasty_tabs.web.dto;

import java.math.BigDecimal;

public record ServerPerformanceDto(
        String serverEmail,
        Long totalAssignments,
        Long ordersProcessed,
        BigDecimal totalRevenueGenerated,
        Integer revenueRank,
        Integer ordersRank,
        BigDecimal avgOrdersPerShift,
        BigDecimal avgOrderValue
) {}
