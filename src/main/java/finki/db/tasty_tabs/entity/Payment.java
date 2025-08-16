package finki.db.tasty_tabs.entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity: Payment
 * Description: Represents payments for orders.
 */
@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tip_amount")
    private Double tipAmount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "payment_type", nullable = false)
    private String paymentType;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false, unique = true)
    private Order order;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(Double tipAmount) {
        this.tipAmount = tipAmount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}