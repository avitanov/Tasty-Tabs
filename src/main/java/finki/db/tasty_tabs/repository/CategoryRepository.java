package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT * FROM category WHERE id = :id", nativeQuery = true)
    Category customFindCategoryById(@Param("id") Long id);

    @Query(value = "SELECT * FROM category", nativeQuery = true)
    List<Category> customFindAllCategories();

    // Custom INSERT (note: JPA doesn't usually do INSERT via query, so avoid this if possible)
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO category (name, is_available) VALUES (:name, :isAvailable)", nativeQuery = true)
    void customInsertCategory(@Param("name") String name, @Param("isAvailable") Boolean isAvailable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE category SET name = :name, is_available = :isAvailable WHERE id = :id", nativeQuery = true)
    int customUpdateCategory(@Param("id") Long id,
                       @Param("name") String name,
                       @Param("isAvailable") Boolean isAvailable);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM category WHERE id = :id", nativeQuery = true)
    int customDeleteCategoryById(@Param("id") Long id);

    @Query(value = "SELECT * FROM category WHERE name = :name", nativeQuery = true)
    Category customFindCategoryByName(@Param("name") String name);
}
