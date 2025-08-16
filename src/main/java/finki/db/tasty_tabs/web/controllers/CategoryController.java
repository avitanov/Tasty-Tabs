package finki.db.tasty_tabs.web.controllers;

import finki.db.tasty_tabs.entity.Category;
import finki.db.tasty_tabs.service.CategoryService;
import finki.db.tasty_tabs.web.dto.CategoryDto;
import finki.db.tasty_tabs.web.dto.CreateCategoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category API", description = "Endpoints for managing product categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Get all categories")
    @GetMapping
    public ResponseEntity<List<CategoryDto>> findAll() {
        List<CategoryDto> categories = categoryService.getAllCategories().stream()
                .map(CategoryDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Get category by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> findById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        return ResponseEntity.ok(CategoryDto.from(category));
    }

    @Operation(summary = "Create category")
    @PostMapping("/add")
    public ResponseEntity<CategoryDto> save(@RequestBody CreateCategoryDto createCategoryDto) {
        Category saved = categoryService.createCategory(createCategoryDto.toCategory());
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoryDto.from(saved));
    }

    @Operation(summary = "Update category")
    @PutMapping("/edit/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable Long id, @RequestBody CreateCategoryDto createCategoryDto) {
        Category updated = categoryService.updateCategory(id, createCategoryDto.toCategory());
        return ResponseEntity.ok(CategoryDto.from(updated));
    }

    @Operation(summary = "Delete category")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
