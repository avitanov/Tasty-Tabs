package finki.db.tasty_tabs.service;

import finki.db.tasty_tabs.entity.Product;
import finki.db.tasty_tabs.web.dto.CreateProductDto;

import java.util.List;

public interface ProductService {
    Product findById(Long id);
    List<Product> getAllProducts();
    Product updateProduct(Long id, CreateProductDto dto);
    void deleteProduct(Long id);
    List<Product> getProductsByName(String name);
    Product createProduct(CreateProductDto dto);
}