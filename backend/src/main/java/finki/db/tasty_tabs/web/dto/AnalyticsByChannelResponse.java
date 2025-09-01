package finki.db.tasty_tabs.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AnalyticsByChannelResponse(
        LocalDate from,
        LocalDate to,
        List<ChannelTableDto> channels,
        Long grandTotalPaidOrders,
        BigDecimal grandTotalRevenue,
        BigDecimal grandTotalTips
) {}
