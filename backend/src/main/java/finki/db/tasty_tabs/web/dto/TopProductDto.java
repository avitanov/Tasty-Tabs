package finki.db.tasty_tabs.web.dto;

import java.math.BigDecimal;

public record TopProductDto(
        Long productId,
        String productName,
        String categoryName,
        Long totalQuantitySold,
        BigDecimal totalRevenue,
        BigDecimal revenueSharePercent
) {}
