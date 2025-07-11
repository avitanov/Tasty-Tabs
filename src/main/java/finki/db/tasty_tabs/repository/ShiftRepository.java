package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRepository extends JpaRepository<Shift, Long> {}
