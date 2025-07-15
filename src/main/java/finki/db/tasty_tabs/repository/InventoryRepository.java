package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {
}
