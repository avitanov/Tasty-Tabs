package finki.db.tasty_tabs.entity.exceptions;

public class EmployeeNotFoundException extends DomainException {

    public EmployeeNotFoundException(Long id) {
        super(String.format("Employee with id %d doesnt exist",id));
    }
}
