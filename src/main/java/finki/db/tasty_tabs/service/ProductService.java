package finki.db.tasty_tabs.service;

import finki.db.tasty_tabs.entity.Category;
import finki.db.tasty_tabs.entity.Product;

import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    List<Product> getAllProducts();
    Category createCategory(Category category);
    List<Category> getAllCategories();
}