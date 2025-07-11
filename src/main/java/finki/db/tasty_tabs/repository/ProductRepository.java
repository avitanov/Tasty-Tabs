package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}
