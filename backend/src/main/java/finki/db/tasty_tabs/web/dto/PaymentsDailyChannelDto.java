package finki.db.tasty_tabs.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentsDailyChannelDto(
        LocalDate day,
        String channel,
        Long paidOrdersCnt,
        BigDecimal revenue,
        BigDecimal tipTotal
) {}
