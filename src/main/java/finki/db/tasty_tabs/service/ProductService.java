package finki.db.tasty_tabs.service;

import finki.db.tasty_tabs.entity.Category;
import finki.db.tasty_tabs.entity.Product;
import finki.db.tasty_tabs.web.dto.CreateProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<Product> findById(Long id);
    List<Product> getAllProducts();
    Optional<Product> updateProduct(Long id, CreateProductDto dto);
    void deleteProduct(Long id);
    List<Product> getProductsByName(String name);
    Optional<Product> createProduct(CreateProductDto dto);
}