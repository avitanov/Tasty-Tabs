package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {}
