package finki.db.tasty_tabs.service.impl;

import finki.db.tasty_tabs.repository.AnalyticsReadRepository;
import finki.db.tasty_tabs.service.AnalyticsExtraService;
import finki.db.tasty_tabs.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsExtraServiceImpl implements AnalyticsExtraService {

    private final AnalyticsReadRepository repo;

    @Override public List<ServerPerformanceDto> serverPerformanceLast3Months() {
        return repo.serverPerformanceLast3Months();
    }
    @Override public List<MonthlyRevenueVsLaborDto> monthlyRevenueVsLabor() {
        return repo.monthlyRevenueVsLabor();
    }
    @Override public List<DailyOpsDto> dailyOpsSummary(int daysBack) {
        return repo.dailyOpsSummary(daysBack <= 0 ? 30 : daysBack);
    }
    @Override public List<TopProductDto> topProducts(int daysBack, int limit) {
        return repo.topProducts(daysBack <= 0 ? 90 : daysBack, limit <= 0 ? 10 : limit);
    }
    @Override public List<Map<String, Object>> revenueByShiftPeriodView() {
        return repo.revenueByShiftPeriodView();
    }
    @Override public List<RevenueSplitDto> revenueSplit(LocalDate from, LocalDate to) {
        if (from == null) from = LocalDate.now().minusDays(30);
        if (to == null)   to   = LocalDate.now();
        return repo.revenueSplit(from, to);
    }
    @Override public List<ManagerShiftAboveAvgDto> managersShiftsAboveMonthlyAvgCurrentYear() {
        return repo.managersShiftsAboveMonthlyAvgCurrentYear();
    }
}