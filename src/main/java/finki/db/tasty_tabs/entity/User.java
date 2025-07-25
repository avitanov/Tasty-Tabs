package finki.db.tasty_tabs.entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entity: User
 * Description: Contains main information about all users in the system.
 */
@Entity
@Table(name = "users") // "user" is often a reserved keyword in SQL
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED) // Strategy to allow for Employee/Customer subtypes
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;

    // Relationship for Reservations made by a user (if they are a customer)
    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;
}
