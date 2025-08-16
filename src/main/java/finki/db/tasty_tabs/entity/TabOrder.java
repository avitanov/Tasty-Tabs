package finki.db.tasty_tabs.entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Entity: TabOrder
 * Description: Represents orders made at a specific table.
 */
@Entity
@Table(name = "tab_orders")
@Data
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "order_id")
public class TabOrder extends Order {

    @ManyToOne
    @JoinColumn(name = "table_number", referencedColumnName = "table_number", nullable = false)
    private RestaurantTable restaurantTable;

    @ManyToOne
    @JoinColumn(name = "front_staff_id", referencedColumnName = "employee_id", nullable = false)
    private FrontStaff frontStaff;

    public RestaurantTable getRestaurantTable() {
        return restaurantTable;
    }

    public void setRestaurantTable(RestaurantTable restaurantTable) {
        this.restaurantTable = restaurantTable;
    }

    public FrontStaff getFrontStaff() {
        return frontStaff;
    }

    public void setFrontStaff(FrontStaff frontStaff) {
        this.frontStaff = frontStaff;
    }
}