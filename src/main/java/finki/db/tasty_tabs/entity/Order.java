package finki.db.tasty_tabs.entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity: Order
 * Description: Represents a customer's order in the system.
 */
@Entity
@Table(name = "orders") // "order" is a reserved keyword
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "datetime", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}