package finki.db.tasty_tabs.web.dto;

import finki.db.tasty_tabs.entity.Payment;

import java.time.LocalDateTime;

public record PaymentDto(
        Long id,
        Double tipAmount,
        LocalDateTime timestamp,
        String paymentType,
        Double amount,
        Long orderId
) {
    public static PaymentDto from(Payment payment) {
        return new PaymentDto(
                payment.getId(),
                payment.getTipAmount(),
                payment.getTimestamp(),
                payment.getPaymentType(),
                payment.getAmount(),
                payment.getOrder().getId()
        );
    }
}
