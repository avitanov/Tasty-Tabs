package finki.db.tasty_tabs.entity.exceptions;

public class OrderNotFoundException extends DomainException {

    public OrderNotFoundException(Long id) {
        super(String.format("Order with id %d doesnt exist",id));
    }
}
