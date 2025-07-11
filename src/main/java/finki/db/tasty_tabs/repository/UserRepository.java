package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
