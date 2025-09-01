package finki.db.tasty_tabs.service.impl;

import finki.db.tasty_tabs.entity.*;
import finki.db.tasty_tabs.entity.composite_keys.ReservationManagedFrontStaffId;
import finki.db.tasty_tabs.entity.exceptions.ReservationNotFoundException;
import finki.db.tasty_tabs.entity.exceptions.TableNotFoundException;
import finki.db.tasty_tabs.repository.*;
import finki.db.tasty_tabs.service.ReservationService;
import finki.db.tasty_tabs.web.dto.CreateReservationDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final FrontStaffRepository frontStaffRepository;
    private final RestaurantTableRepository restaurantTableRepository;
    private final ReservationManagedFrontStaffRepository reservationManagedFrontStaffRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository, UserRepository userRepository, FrontStaffRepository frontStaffRepository, RestaurantTableRepository restaurantTableRepository, ReservationManagedFrontStaffRepository reservationManagedFrontStaffRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.frontStaffRepository = frontStaffRepository;
        this.restaurantTableRepository = restaurantTableRepository;
        this.reservationManagedFrontStaffRepository = reservationManagedFrontStaffRepository;
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

    @Override
    public List<Reservation> getAllReservationsByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + userEmail + " not found."));
        return reservationRepository.findAllByUser(user);
    }

    @Override
    public ReservationManagedFrontStaff acceptReservation(Long reservationId, String frontStaffEmail, Long tableNumber) {
        User user = userRepository.findByEmail(frontStaffEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + frontStaffEmail + " not found."));

        if (!(user instanceof FrontStaff)) {
            throw new SecurityException("User is not authorized to accept reservations as they are not a FrontStaff member.");
        }
        FrontStaff frontStaff = (FrontStaff) user;
        // 2. Fetch all other required entities
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        RestaurantTable table = restaurantTableRepository.findById(tableNumber)
                .orElseThrow(() -> new TableNotFoundException(tableNumber));

        // 3. Create the new ReservationManagedFrontStaff entity
        ReservationManagedFrontStaffId id = new ReservationManagedFrontStaffId(
                reservation.getId(),
                frontStaff.getId(),
                table.getTableNumber().longValue()
        );

        ReservationManagedFrontStaff managedReservation = new ReservationManagedFrontStaff();
        managedReservation.setReservation(reservation);
        managedReservation.setFrontStaff(frontStaff);
        managedReservation.setRestaurantTable(table);
        managedReservation.setId(id);

        // 4. Save the new entity to the database
        return reservationManagedFrontStaffRepository.save(managedReservation);
    }
    @Override
    public List<Reservation> getAllReservationsForToday() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);

        return reservationRepository.findAllByDatetimeBetween(startOfDay, endOfDay);
    }

    @Override
    public List<Reservation> getAllReservationsForDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        return reservationRepository.findAllByDatetimeBetween(startOfDay, endOfDay);
    }

}

