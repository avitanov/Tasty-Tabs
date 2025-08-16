package finki.db.tasty_tabs.web.controllers;

import finki.db.tasty_tabs.entity.Product;
import finki.db.tasty_tabs.service.ProductService;
import finki.db.tasty_tabs.web.dto.CreateProductDto;
import finki.db.tasty_tabs.web.dto.ProductDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<ProductDto>> findAll() {
        List<ProductDto> products = productService.getAllProducts().stream()
                .map(ProductDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ProductDto.from(productService.findById(id)));
    }

    @Operation(summary = "Search products by name")
    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> findByName(@RequestParam String name) {
        List<ProductDto> products = productService.getProductsByName(name).stream()
                .map(ProductDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Create new product")
    @PostMapping("/add")
    public ResponseEntity<ProductDto> save(@RequestBody CreateProductDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductDto.from(productService.createProduct(dto)));
    }

    @Operation(summary = "Update product")
    @PutMapping("/edit/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody CreateProductDto dto) {
        var updatedProduct = productService.updateProduct(id, dto);
        return ResponseEntity.ok(ProductDto.from(updatedProduct));
    }

    @Operation(summary = "Delete product")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
