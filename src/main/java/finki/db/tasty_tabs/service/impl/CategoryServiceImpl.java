package finki.db.tasty_tabs.service.impl;

import finki.db.tasty_tabs.entity.Category;
import finki.db.tasty_tabs.repository.CategoryRepository;
import finki.db.tasty_tabs.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> updateCategory(Long id, Category category) {
        return categoryRepository.findById(id).map(existingCategory -> {
            if (category.getName() != null) {
                existingCategory.setName(category.getName());
            }
            if (category.getIsAvailable() != null) {
                existingCategory.setIsAvailable(category.getIsAvailable());
            }
            return categoryRepository.save(existingCategory);
        });
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Optional<Category> createCategory(Category category) {
        return Optional.of(categoryRepository.save(category));
    }
}
