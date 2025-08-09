package finki.db.tasty_tabs.web.controllers;

import finki.db.tasty_tabs.entity.Category;
import finki.db.tasty_tabs.service.CategoryService;
import finki.db.tasty_tabs.web.dto.CreateCategoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<Category> findAll() {
        return categoryService.getAllCategories();
    }

    @Operation(summary = "Get category by ID")
    @GetMapping("/{id}")
    public Category findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @Operation(summary = "Create category")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Category save(@RequestBody CreateCategoryDto createCategoryDto) {
        return categoryService.createCategory(createCategoryDto.toCategory());
    }

    @Operation(summary = "Update category")
    @PutMapping("/edit/{id}")
    public Category update(@PathVariable Long id, @RequestBody CreateCategoryDto createCategoryDto) {
        return categoryService.updateCategory(id, createCategoryDto.toCategory());
    }

    @Operation(summary = "Delete category")
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
