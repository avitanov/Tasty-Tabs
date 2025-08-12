package finki.db.tasty_tabs.entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity: Reservation
 * Description: Represents reservations made by users.
 */
@Entity
@Table(name = "reservation")
@Data
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stay_length")
    private Integer stayLength;

    @Column(name = "datetime", nullable = false)
    private LocalDateTime datetime;

    @Column(name = "creation_timestamp", nullable = false)
    private LocalDateTime creationTimestamp;

    @Column(name = "number_of_people", nullable = false)
    private Integer numberOfPeople;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "reservation")
    private List<ReservationManagedFrontStaff> managedReservations;
}