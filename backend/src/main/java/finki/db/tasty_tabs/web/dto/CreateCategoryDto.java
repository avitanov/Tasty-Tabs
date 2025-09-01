package finki.db.tasty_tabs.web.dto;

import finki.db.tasty_tabs.entity.Category;

public record CreateCategoryDto(
        String name,
        Boolean isAvailable
) {
    public Category toCategory() {
        return new Category(name, isAvailable);
    }

}
