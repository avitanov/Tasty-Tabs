package finki.db.tasty_tabs.entity.exceptions;

public class ShiftNotFoundException extends DomainException {

    public ShiftNotFoundException(Long id) {
        super(String.format("Shift with id %d doesnt exist",id));
    }
}
