package finki.db.tasty_tabs.web.dto;

public record CreateProductDto(
        String name,
        Double price,
        String taxClass,
        String description,
        Boolean manageInventory,
        Long categoryId,
        Integer quantity,
        Integer restockLevel
) {
}
