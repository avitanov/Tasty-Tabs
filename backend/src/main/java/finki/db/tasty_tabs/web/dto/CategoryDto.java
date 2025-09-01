package finki.db.tasty_tabs.web.dto;

import finki.db.tasty_tabs.entity.Category;

public record CategoryDto(
        Long id,
        String name,
        Boolean isAvailable
) {
    public static CategoryDto from(Category category) {
        return new CategoryDto(category.getId(), category.getName(), category.getIsAvailable());
    }
}
