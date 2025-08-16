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
@Table(name = "reservations")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStayLength() {
        return stayLength;
    }

    public void setStayLength(Integer stayLength) {
        this.stayLength = stayLength;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(LocalDateTime creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ReservationManagedFrontStaff> getManagedReservations() {
        return managedReservations;
    }

    public void setManagedReservations(List<ReservationManagedFrontStaff> managedReservations) {
        this.managedReservations = managedReservations;
    }
}