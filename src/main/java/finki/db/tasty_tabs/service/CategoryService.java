package finki.db.tasty_tabs.service;

import finki.db.tasty_tabs.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Optional<Category> findById(Long id);
    List<Category> getAllCategories();
    Optional<Category> updateCategory(Long id,Category category);
    void deleteCategory(Long id);
    Optional<Category> getCategoryByName(String name);
    Optional<Category> createCategory(Category category);
}
