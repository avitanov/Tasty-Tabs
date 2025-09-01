package finki.db.tasty_tabs.service.impl;


import finki.db.tasty_tabs.entity.PaymentsDailyChannel;
import finki.db.tasty_tabs.repository.AnalyticsAdminRepository;
import finki.db.tasty_tabs.repository.PaymentsDailyChannelRepository;
import finki.db.tasty_tabs.service.AnalyticsService;
import finki.db.tasty_tabs.web.dto.AnalyticsByChannelResponse;
import finki.db.tasty_tabs.web.dto.ChannelTableDto;
import finki.db.tasty_tabs.web.dto.PaymentsDailyChannelDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final PaymentsDailyChannelRepository mvRepo;
    private final AnalyticsAdminRepository adminRepo;

    public AnalyticsServiceImpl(PaymentsDailyChannelRepository mvRepo, AnalyticsAdminRepository adminRepo) {
        this.mvRepo = mvRepo;
        this.adminRepo = adminRepo;
    }

    @Override
    public AnalyticsByChannelResponse getPaymentsDailyChannel(LocalDate from, LocalDate to) {
        if (from == null) from = LocalDate.now().minusDays(30);
        if (to == null)   to   = LocalDate.now();

        // pass null channel to fetch ALL channels (JPQL has :channel is null guard)
        List<PaymentsDailyChannel> rows = mvRepo.findRange(from, to, null);

        // group by channel
        Map<String, List<PaymentsDailyChannel>> byChannel = rows.stream()
                .collect(Collectors.groupingBy(PaymentsDailyChannel::getChannel));

        // build per-channel tables
        List<ChannelTableDto> channels = byChannel.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // ONLINE, TAB, UNKNOWN (alphabetical)
                .map(e -> {
                    var data = e.getValue().stream()
                            .sorted(Comparator.comparing(PaymentsDailyChannel::getDay).thenComparing(PaymentsDailyChannel::getChannel))
                            .map(r -> new PaymentsDailyChannelDto(
                                    r.getDay(), r.getChannel(), r.getPaidOrdersCnt(), r.getRevenue(), r.getTipTotal()
                            ))
                            .toList();

                    long totalOrders = e.getValue().stream()
                            .map(PaymentsDailyChannel::getPaidOrdersCnt)
                            .filter(v -> v != null)
                            .mapToLong(Long::longValue)
                            .sum();

                    BigDecimal totalRevenue = e.getValue().stream()
                            .map(PaymentsDailyChannel::getRevenue)
                            .filter(v -> v != null)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal totalTips = e.getValue().stream()
                            .map(PaymentsDailyChannel::getTipTotal)
                            .filter(v -> v != null)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return new ChannelTableDto(e.getKey(), data, totalOrders, totalRevenue, totalTips);
                })
                .toList();

        // grand totals
        long grandOrders = rows.stream()
                .map(PaymentsDailyChannel::getPaidOrdersCnt)
                .filter(v -> v != null)
                .mapToLong(Long::longValue)
                .sum();

        BigDecimal grandRevenue = rows.stream()
                .map(PaymentsDailyChannel::getRevenue)
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal grandTips = rows.stream()
                .map(PaymentsDailyChannel::getTipTotal)
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new AnalyticsByChannelResponse(from, to, channels, grandOrders, grandRevenue, grandTips);
    }}