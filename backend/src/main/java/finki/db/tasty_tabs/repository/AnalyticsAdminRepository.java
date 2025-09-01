package finki.db.tasty_tabs.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class AnalyticsAdminRepository {

    private final JdbcTemplate jdbcTemplate;

    /** Non-blocking for readers; must run outside a transaction */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void refreshMvPaymentsDailyChannelConcurrently() {
        jdbcTemplate.update("REFRESH MATERIALIZED VIEW CONCURRENTLY mv_payments_daily_channel");
    }

    /** Blocking for readers; can run inside a transaction */
    @Transactional
    public void refreshMvPaymentsDailyChannel() {
        jdbcTemplate.update("REFRESH MATERIALIZED VIEW mv_payments_daily_channel");
    }
}