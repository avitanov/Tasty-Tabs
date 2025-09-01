package finki.db.tasty_tabs.entity.exceptions;

public class OrderItemNotFoundException extends DomainException {

    public OrderItemNotFoundException() {
        super(String.format("OrderItem doesnt exist"));
    }
}