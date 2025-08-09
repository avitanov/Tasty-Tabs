package finki.db.tasty_tabs.service.impl;

import finki.db.tasty_tabs.entity.Reservation;
import finki.db.tasty_tabs.entity.User;
import finki.db.tasty_tabs.entity.exceptions.ReservationNotFoundException;
import finki.db.tasty_tabs.repository.ReservationRepository;
import finki.db.tasty_tabs.repository.UserRepository;
import finki.db.tasty_tabs.service.ReservationService;
import finki.db.tasty_tabs.web.dto.CreateReservationDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    @Override
    public Reservation createReservation(CreateReservationDto dto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + userEmail + " not found."));

        Reservation reservation = new Reservation();
        reservation.setStayLength(dto.stayLength());
        reservation.setDatetime(dto.datetime());
        reservation.setNumberOfPeople(dto.numberOfPeople());
        reservation.setUser(user);
        reservation.setCreationTimestamp(LocalDateTime.now());

        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation updateReservation(Long id, CreateReservationDto dto, String userEmail) {
        Reservation existing = getReservationById(id);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + userEmail + " not found."));

        // Optionally, add a check if the logged-in user is allowed to update this reservation
        if (!existing.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You are not authorized to update this reservation.");
        }

        existing.setStayLength(dto.stayLength());
        existing.setDatetime(dto.datetime());
        existing.setNumberOfPeople(dto.numberOfPeople());
        existing.setUser(user);

        return reservationRepository.save(existing);
    }

    @Override
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}

