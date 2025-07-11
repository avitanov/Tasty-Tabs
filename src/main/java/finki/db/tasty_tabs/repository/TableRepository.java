package finki.db.tasty_tabs.repository;

import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<Table, Integer> {}
