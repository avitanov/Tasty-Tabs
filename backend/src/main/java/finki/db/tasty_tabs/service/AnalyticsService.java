package finki.db.tasty_tabs.service;

import finki.db.tasty_tabs.web.dto.AnalyticsByChannelResponse;

import java.time.LocalDate;

public interface AnalyticsService {
    AnalyticsByChannelResponse getPaymentsDailyChannel(LocalDate from, LocalDate to);
}
