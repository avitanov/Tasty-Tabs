package finki.db.tasty_tabs.service;

import finki.db.tasty_tabs.entity.Payment;
import finki.db.tasty_tabs.web.dto.CreatePaymentDto;

import java.util.List;

public interface PaymentService {
    Payment createPayment(CreatePaymentDto dto);
    Payment findPaymentById(Long id);
    Payment findPaymentByOrderId(Long orderId);
    List<Payment> findAllPayments();
    Payment updatePayment(Long id, CreatePaymentDto dto);
}
