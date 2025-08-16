package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Find a payment by its associated order ID
    Optional<Payment> findByOrderId(Long orderId);
}
