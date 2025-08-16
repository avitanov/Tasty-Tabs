package finki.db.tasty_tabs.entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entity: Customer
 * Description: Represents customers in the system. Inherits from User.
 */
@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id") // Links to the User table
public class Customer extends User {
    
    // Relationship for online orders placed by this customer
    @OneToMany(mappedBy = "customer")
    private List<OnlineOrder> onlineOrders;
}