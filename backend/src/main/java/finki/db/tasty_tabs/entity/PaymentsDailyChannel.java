package finki.db.tasty_tabs.entity;

import finki.db.tasty_tabs.entity.composite_keys.PaymentsDailyChannelId;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "mv_payments_daily_channel")
@IdClass(PaymentsDailyChannelId.class)
@Immutable
public class PaymentsDailyChannel {

    @Id
    private LocalDate day;

    @Id
    private String channel; // 'TAB' | 'ONLINE' | 'UNKNOWN'

    @Column(name = "paid_orders_cnt")
    private Long paidOrdersCnt;

    private BigDecimal revenue;

    @Column(name = "tip_total")
    private BigDecimal tipTotal;

    // getters only (or no setters) to keep it immutable in code
    public LocalDate getDay() { return day; }
    public String getChannel() { return channel; }
    public Long getPaidOrdersCnt() { return paidOrdersCnt; }
    public BigDecimal getRevenue() { return revenue; }
    public BigDecimal getTipTotal() { return tipTotal; }
}

