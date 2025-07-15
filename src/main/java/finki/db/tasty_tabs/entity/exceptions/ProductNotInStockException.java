package finki.db.tasty_tabs.entity.exceptions;

public class ProductNotInStockException extends RuntimeException {

    public ProductNotInStockException(String name) {
        super(String.format("Product: %s not in stock",name));
    }
}

