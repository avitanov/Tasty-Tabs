package finki.db.tasty_tabs.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entity: Table
 * Description: Represents dining tables in the system.
 */
@Entity
@Table(name = "tables") // "table" is a reserved keyword
@Data
@NoArgsConstructor
public class RestaurantTable {

    @Id
    @Column(name = "table_number")
    private Integer tableNumber;

    @Column(name = "seat_capacity", nullable = false)
    private Integer seatCapacity;

    @OneToMany(mappedBy = "table")
    private List<TabOrder> tabOrders;

    public RestaurantTable(Integer tableNumber, Integer seatCapacity) {
        this.tableNumber=tableNumber;
        this.seatCapacity=seatCapacity;
    }
}

