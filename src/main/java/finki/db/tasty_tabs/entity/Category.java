package finki.db.tasty_tabs.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entity: Category
 */
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Product> products;

    public Category(String name, Boolean isAvailable) {
        this.name = name;
        this.isAvailable = isAvailable;
    }
}
