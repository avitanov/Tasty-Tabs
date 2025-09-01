package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {}
