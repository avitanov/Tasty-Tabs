package finki.db.tasty_tabs.web.controllers;
import finki.db.tasty_tabs.service.AnalyticsExtraService;
import finki.db.tasty_tabs.service.AnalyticsService;
import finki.db.tasty_tabs.web.dto.AnalyticsByChannelResponse;
import finki.db.tasty_tabs.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final AnalyticsExtraService analyticsExtraService;

    // Existing MV endpoint
    @GetMapping("/reveunueByChannel")
    public AnalyticsByChannelResponse paymentsDailyChannel(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return analyticsService.getPaymentsDailyChannel(from, to);
    }

    // 1) Server performance & revenue ranking (last 3 months)
    @GetMapping("/servers/performance")
    public List<ServerPerformanceDto> serverPerformance() {
        return analyticsExtraService.serverPerformanceLast3Months();
    }

    // 2) Monthly revenue vs labor cost
    @GetMapping("/monthly-revenue-vs-labor")
    public List<MonthlyRevenueVsLaborDto> monthlyRevenueVsLabor() {
        return analyticsExtraService.monthlyRevenueVsLabor();
    }

    // 3) Daily operations summary (default 30 days)
    @GetMapping("/daily-ops")
    public List<DailyOpsDto> dailyOps(@RequestParam(defaultValue = "30") int days) {
        return analyticsExtraService.dailyOpsSummary(days);
    }

    // 4) Top products (defaults: last 90 days, top 10)
    @GetMapping("/top-products")
    public List<TopProductDto> topProducts(
            @RequestParam(defaultValue = "90") int days,
            @RequestParam(defaultValue = "10") int limit) {
        return analyticsExtraService.topProducts(days, limit);
    }

    // 5) Revenue by shift period (dynamic view -> generic rows)
    @GetMapping("/revenue-by-shift-period")
    public List<Map<String, Object>> revenueByShiftPeriod() {
        return analyticsExtraService.revenueByShiftPeriodView();
    }

    // 6) Revenue split (calls your SQL function) by order date
    @GetMapping("/revenue-split")
    public List<RevenueSplitDto> revenueSplit(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return analyticsExtraService.revenueSplit(from, to);
    }

    // 7) Managers' shifts above monthly average (current year)
    @GetMapping("/managers/shifts-above-avg")
    public List<ManagerShiftAboveAvgDto> shiftsAboveAvg() {
        return analyticsExtraService.managersShiftsAboveMonthlyAvgCurrentYear();
    }
}
