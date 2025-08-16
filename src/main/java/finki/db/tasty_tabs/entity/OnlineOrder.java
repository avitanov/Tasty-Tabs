package finki.db.tasty_tabs.entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Entity: OnlineOrder
 * Description: Represents orders made online by customers.
 */
@Entity
@Table(name = "online_orders")
@Data
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "order_id")
public class OnlineOrder extends Order {

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "user_id", nullable = false)
    private Customer customer;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
