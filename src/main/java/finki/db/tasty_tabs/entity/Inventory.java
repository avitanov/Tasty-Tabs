package finki.db.tasty_tabs.entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Entity: Inventory
 * Description: Tracks product quantities and restocking levels.
 */
@Entity
@Table(name = "inventories")
@Data
@NoArgsConstructor
public class Inventory {

    @Id
    @Column(name = "product_id")
    private Long productId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "restock_level")
    private Integer restockLevel;

    public Inventory(Long id, Integer quantity, Integer restockLevel) {
        this.productId=id;
        this.quantity=quantity;
        this.restockLevel=restockLevel;
    }
}
