package finki.db.tasty_tabs.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity: Assignment
 * Description: Represents shift assignment for each employee.
 */
@Entity
@Table(name = "assignments")
@Data
@NoArgsConstructor
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clock_in_time")
    private LocalDateTime clockInTime;

    @Column(name = "clock_out_time")
    private LocalDateTime clockOutTime;

    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "employee_id", nullable = false)
    private Manager manager;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "user_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "shift_id", referencedColumnName = "id", nullable = false)
    private Shift shift;

    public Assignment(LocalDateTime clockInTime, LocalDateTime clockOutTime, Employee employee, Shift shift) {
        this.clockInTime = clockInTime;
        this.clockOutTime = clockOutTime;
        this.employee = employee;
        this.shift = shift;
    }

    public Assignment(LocalDateTime clockInTime, LocalDateTime clockOutTime, Manager manager, Employee employee, Shift shift) {
        this.clockInTime = clockInTime;
        this.clockOutTime = clockOutTime;
        this.manager = manager;
        this.employee = employee;
        this.shift = shift;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getClockInTime() {
        return clockInTime;
    }

    public void setClockInTime(LocalDateTime clockInTime) {
        this.clockInTime = clockInTime;
    }

    public LocalDateTime getClockOutTime() {
        return clockOutTime;
    }

    public void setClockOutTime(LocalDateTime clockOutTime) {
        this.clockOutTime = clockOutTime;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }
}