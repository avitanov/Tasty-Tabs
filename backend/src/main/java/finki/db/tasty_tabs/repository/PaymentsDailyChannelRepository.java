package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.PaymentsDailyChannel;
import finki.db.tasty_tabs.entity.composite_keys.PaymentsDailyChannelId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface PaymentsDailyChannelRepository
        extends JpaRepository<PaymentsDailyChannel, PaymentsDailyChannelId> {

    @Query("select p from PaymentsDailyChannel p " +
            "where p.day between :from and :to " +
            "and (:channel is null or p.channel = :channel) " +
            "order by p.day asc, p.channel asc")
    List<PaymentsDailyChannel> findRange(LocalDate from, LocalDate to, String channel);
}
