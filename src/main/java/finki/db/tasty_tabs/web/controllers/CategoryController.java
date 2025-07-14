package finki.db.tasty_tabs.web.controllers;

import finki.db.tasty_tabs.entity.Category;
import finki.db.tasty_tabs.service.CategoryService;
import finki.db.tasty_tabs.web.dto.CreateCategoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category API", description = "Endpoints for managing product categories") // OpenAPI tag
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Get all categories", description = "Retrieves a list of all available categories.")
    @GetMapping
    public List<Category> findAll() {
        return categoryService.getAllCategories();
    }

    @Operation(summary = "Get categoryId by ID", description = "Finds a categoryId by its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id) {
        return categoryService.findById(id)
                .map(category -> ResponseEntity.ok().body(category))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Add a new categoryId", description = "Creates a new categoryId.")
    @PostMapping("/add")
    public ResponseEntity<Category> save(@RequestBody CreateCategoryDto createCategoryDto) {
        return categoryService.createCategory(createCategoryDto.toCategory())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update an existing categoryId", description = "Updates a categoryId by ID.")
    @PutMapping("/edit/{id}")
    public ResponseEntity<Category> update(
            @PathVariable Long id,
            @RequestBody CreateCategoryDto createCategoryDto
    ) {
        return categoryService.updateCategory(id, createCategoryDto.toCategory())
                .map(category -> ResponseEntity.ok().body(category))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a categoryId", description = "Deletes a categoryId by its ID.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (categoryService.findById(id).isPresent()) {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
