package finki.db.tasty_tabs.entity.exceptions;

public class InvalidOrderTypeException extends DomainException {

    public InvalidOrderTypeException(String message) {
        super(message);
    }
}
