package finki.db.tasty_tabs.web.dto;

import finki.db.tasty_tabs.entity.Product;

public record ProductDto(
        Long id,
        String name,
        Double price,
        String taxClass,
        String description,
        Boolean manageInventory,
        CategoryDto category
) {
    public static ProductDto from(Product product) {
        return new ProductDto(product.getId(),
                product.getName(),
                product.getPrice(),
                product.getTaxClass(),
                product.getDescription(),
                product.getManageInventory(),
                CategoryDto.from(product.getCategory())
                );
    }
}

