package finki.db.tasty_tabs.web.dto;

import java.math.BigDecimal;
import java.util.List;

public record ChannelTableDto(
        String channel,
        List<PaymentsDailyChannelDto> data,
        Long totalPaidOrders,
        BigDecimal totalRevenue,
        BigDecimal totalTips
) {}
