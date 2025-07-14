package finki.db.tasty_tabs.entity.exceptions;

public class TableNumberAlreadyExistsException extends RuntimeException {

    public TableNumberAlreadyExistsException(Integer number) {
        super(String.format("Table: %d already exists", number));
    }
}
