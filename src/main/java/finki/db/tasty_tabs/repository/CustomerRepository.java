package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {}
