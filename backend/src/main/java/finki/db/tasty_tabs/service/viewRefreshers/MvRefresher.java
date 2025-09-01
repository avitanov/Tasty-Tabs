package finki.db.tasty_tabs.service.viewRefreshers;

import finki.db.tasty_tabs.repository.AnalyticsAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
@RequiredArgsConstructor
public class MvRefresher {
    private final AnalyticsAdminRepository analyticsAdminRepository;

    public void refreshPaymentsMvAfterCommit() {
        // If there is an active transaction, run after commit; otherwise run immediately.
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    analyticsAdminRepository.refreshMvPaymentsDailyChannelConcurrently();
                }
            });
        } else {
            analyticsAdminRepository.refreshMvPaymentsDailyChannelConcurrently();
        }
    }
}
