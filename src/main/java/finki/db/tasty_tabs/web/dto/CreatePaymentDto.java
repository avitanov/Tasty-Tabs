package finki.db.tasty_tabs.web.dto;

public record CreatePaymentDto(
        Double tipAmount,
        String paymentType,
        Double amount,
        Long orderId
) {}
