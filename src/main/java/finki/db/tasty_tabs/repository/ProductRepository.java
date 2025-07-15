package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByName(String name);
}
