package finki.db.tasty_tabs.service;

import finki.db.tasty_tabs.web.dto.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AnalyticsExtraService {
    List<ServerPerformanceDto> serverPerformanceLast3Months();
    List<MonthlyRevenueVsLaborDto> monthlyRevenueVsLabor();
    List<DailyOpsDto> dailyOpsSummary(int daysBack);
    List<TopProductDto> topProducts(int daysBack, int limit);
    List<Map<String,Object>> revenueByShiftPeriodView();
    List<RevenueSplitDto> revenueSplit(LocalDate from, LocalDate to);
    List<ManagerShiftAboveAvgDto> managersShiftsAboveMonthlyAvgCurrentYear();
}
