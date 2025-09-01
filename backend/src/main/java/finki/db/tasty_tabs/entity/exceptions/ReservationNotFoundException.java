package finki.db.tasty_tabs.entity.exceptions;

public class ReservationNotFoundException extends DomainException {

    public ReservationNotFoundException(Long id) {
        super(String.format("Reservation with id %d doesnt exist",id));
    }
}

