package finki.db.tasty_tabs.entity.exceptions;

public class FrontStaffNotFoundException extends DomainException {

    public FrontStaffNotFoundException(Long id) {
        super(String.format("FrontStaff with id %d doesnt exist",id));
    }
}
