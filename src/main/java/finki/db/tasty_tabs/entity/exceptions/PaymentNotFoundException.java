package finki.db.tasty_tabs.entity.exceptions;

public class PaymentNotFoundException extends DomainException {

    public PaymentNotFoundException(Long id) {
        super(String.format("Payment with id %d doesnt exist",id));
    }
}
