package finki.db.tasty_tabs.entity.composite_keys;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class PaymentsDailyChannelId implements Serializable {
    private LocalDate day;
    private String channel;

    public PaymentsDailyChannelId() {}
    public PaymentsDailyChannelId(LocalDate day, String channel) {
        this.day = day; this.channel = channel;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentsDailyChannelId that)) return false;
        return Objects.equals(day, that.day) && Objects.equals(channel, that.channel);
    }
    @Override public int hashCode() { return Objects.hash(day, channel); }
}