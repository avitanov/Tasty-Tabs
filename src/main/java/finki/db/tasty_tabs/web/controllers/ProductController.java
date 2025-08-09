package finki.db.tasty_tabs.web.controllers;

import finki.db.tasty_tabs.entity.Product;
import finki.db.tasty_tabs.service.ProductService;
import finki.db.tasty_tabs.web.dto.CreateProductDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "Get all products")
    @GetMapping
    public List<Product> findAll() {
        return productService.getAllProducts();
    }

    @Operation(summary = "Get product by ID")
    @GetMapping("/{id}")
    public Product findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @Operation(summary = "Search products by name")
    @GetMapping("/search")
    public List<Product> findByName(@RequestParam String name) {
        return productService.getProductsByName(name);
    }

    @Operation(summary = "Create new product")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Product save(@RequestBody CreateProductDto dto) {
        return productService.createProduct(dto);
    }

    @Operation(summary = "Update product")
    @PutMapping("/edit/{id}")
    public Product update(@PathVariable Long id, @RequestBody CreateProductDto dto) {
        return productService.updateProduct(id, dto);
    }

    @Operation(summary = "Delete product")
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
