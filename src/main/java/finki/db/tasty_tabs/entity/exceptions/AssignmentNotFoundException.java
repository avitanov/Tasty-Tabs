package finki.db.tasty_tabs.entity.exceptions;

public class AssignmentNotFoundException extends DomainException {

    public AssignmentNotFoundException(Long id) {
        super(String.format("Assignment with id %d doesnt exist",id));
    }
}
