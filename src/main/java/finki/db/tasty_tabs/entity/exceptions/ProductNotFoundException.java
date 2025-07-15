package finki.db.tasty_tabs.entity.exceptions;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super(String.format("Product with id %d doesnt exist",id));
    }
}

