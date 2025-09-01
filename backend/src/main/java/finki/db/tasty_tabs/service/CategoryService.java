package finki.db.tasty_tabs.service;

import finki.db.tasty_tabs.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category findById(Long id);
    List<Category> getAllCategories();
    Category updateCategory(Long id,Category category);
    void deleteCategory(Long id);
    Category getCategoryByName(String name);
    Category createCategory(Category category);
}
