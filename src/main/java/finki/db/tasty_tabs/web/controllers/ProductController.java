package finki.db.tasty_tabs.web.controllers;
import finki.db.tasty_tabs.entity.Product;
import finki.db.tasty_tabs.service.ProductService;
import finki.db.tasty_tabs.web.dto.CreateProductDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product API", description = "Endpoints for managing products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get all products", description = "Returns a list of all products.")
    @GetMapping
    public List<Product> findAll() {
        return productService.getAllProducts();
    }

    @Operation(summary = "Get product by ID", description = "Finds a product by its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Search products by name", description = "Returns products with a given name.")
    @GetMapping("/search")
    public List<Product> findByName(@RequestParam String name) {
        return productService.getProductsByName(name);
    }

    @Operation(summary = "Create new product", description = "Creates a new product (with or without inventory).")
    @PostMapping("/add")
    public ResponseEntity<Product> save(@RequestBody CreateProductDto dto) {
        return productService.createProduct(dto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Update product", description = "Updates an existing product by ID.")
    @PutMapping("/edit/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody CreateProductDto dto) {
        return productService.updateProduct(id, dto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete product", description = "Deletes a product by its ID.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}

