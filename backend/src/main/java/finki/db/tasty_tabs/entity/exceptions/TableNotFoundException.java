package finki.db.tasty_tabs.entity.exceptions;

public class TableNotFoundException extends DomainException {

    public TableNotFoundException(Long id) {
        super(String.format("Table with id %d doesnt exist",id));
    }
}
