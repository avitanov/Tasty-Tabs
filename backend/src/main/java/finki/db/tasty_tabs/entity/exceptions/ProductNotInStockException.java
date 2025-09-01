package finki.db.tasty_tabs.entity.exceptions;

public class ProductNotInStockException extends DomainException {

    public ProductNotInStockException(String name) {
        super(String.format("Product: %s not in stock",name));
    }
}

