package finki.db.tasty_tabs.service.impl;


import finki.db.tasty_tabs.entity.Order;
import finki.db.tasty_tabs.entity.Payment;
import finki.db.tasty_tabs.entity.exceptions.OrderNotFoundException;
import finki.db.tasty_tabs.entity.exceptions.PaymentNotFoundException;
import finki.db.tasty_tabs.repository.OrderRepository;
import finki.db.tasty_tabs.repository.PaymentRepository;
import finki.db.tasty_tabs.service.PaymentService;
import finki.db.tasty_tabs.web.dto.CreatePaymentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public Payment createPayment(CreatePaymentDto dto) {
        Order order = orderRepository.findById(dto.orderId())
                .orElseThrow(() -> new OrderNotFoundException(dto.orderId()));

        Payment payment = new Payment();
        payment.setAmount(dto.amount());
        payment.setTipAmount(dto.tipAmount());
        payment.setPaymentType(dto.paymentType());
        payment.setTimestamp(LocalDateTime.now());
        payment.setOrder(order);

        return paymentRepository.save(payment);
    }

    @Override
    public Payment findPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));
    }

    @Override
    public Payment findPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentNotFoundException(orderId));
    }

    @Override
    public List<Payment> findAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    @Transactional
    public Payment updatePayment(Long id, CreatePaymentDto dto) {
        Payment existingPayment = findPaymentById(id);

        existingPayment.setAmount(dto.amount());
        existingPayment.setTipAmount(dto.tipAmount());
        existingPayment.setPaymentType(dto.paymentType());

        return paymentRepository.save(existingPayment);
    }

}