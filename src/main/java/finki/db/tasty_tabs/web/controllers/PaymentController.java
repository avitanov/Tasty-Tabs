package finki.db.tasty_tabs.web.controllers;


import finki.db.tasty_tabs.entity.Payment;
import finki.db.tasty_tabs.service.PaymentService;
import finki.db.tasty_tabs.web.dto.CreatePaymentDto;
import finki.db.tasty_tabs.web.dto.PaymentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment API", description = "Endpoints for managing payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Create a new payment for an order")
    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@RequestBody CreatePaymentDto dto) {
        Payment newPayment = paymentService.createPayment(dto);
        return ResponseEntity.ok(PaymentDto.from(newPayment));
    }

    @Operation(summary = "Get a payment by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.findPaymentById(id);
        return ResponseEntity.ok(PaymentDto.from(payment));
    }

    @Operation(summary = "Get a payment by its associated order ID")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentDto> getPaymentByOrderId(@PathVariable Long orderId) {
        Payment payment = paymentService.findPaymentByOrderId(orderId);
        return ResponseEntity.ok(PaymentDto.from(payment));
    }

    @Operation(summary = "Get all payments")
    @GetMapping
    public List<PaymentDto> getAllPayments() {
        return paymentService.findAllPayments().stream()
                .map(PaymentDto::from)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Update an existing payment")
    @PutMapping("/{id}")
    public ResponseEntity<PaymentDto> updatePayment(@PathVariable Long id, @RequestBody CreatePaymentDto dto) {
        Payment updatedPayment = paymentService.updatePayment(id, dto);
        return ResponseEntity.ok(PaymentDto.from(updatedPayment));
    }

}
