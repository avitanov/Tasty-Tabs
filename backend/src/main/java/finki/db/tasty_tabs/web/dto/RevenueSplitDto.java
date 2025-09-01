package finki.db.tasty_tabs.web.dto;


import java.math.BigDecimal;

public record RevenueSplitDto(
        String orderType,            // Online Orders | Tab Orders
        BigDecimal totalRevenue
) {}
