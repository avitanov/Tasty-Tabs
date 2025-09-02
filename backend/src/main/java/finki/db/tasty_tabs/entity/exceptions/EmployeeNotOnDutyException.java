package finki.db.tasty_tabs.entity.exceptions;

public class EmployeeNotOnDutyException extends RuntimeException {
    public EmployeeNotOnDutyException(String message) {
        super(message);
    }
}
