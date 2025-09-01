package finki.db.tasty_tabs.entity.exceptions;

public class TableNumberAlreadyExistsException extends DomainException {

    public TableNumberAlreadyExistsException(Long number) {
        super(String.format("Table: %d already exists", number));
    }
}
