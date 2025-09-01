package finki.db.tasty_tabs.service.impl;

import finki.db.tasty_tabs.entity.Assignment;
import finki.db.tasty_tabs.entity.Employee;
import finki.db.tasty_tabs.entity.Manager;
import finki.db.tasty_tabs.entity.Shift;
import finki.db.tasty_tabs.entity.exceptions.AssignmentNotFoundException;
import finki.db.tasty_tabs.entity.exceptions.EmployeeNotFoundException;
import finki.db.tasty_tabs.entity.exceptions.ShiftNotFoundException;
import finki.db.tasty_tabs.repository.AssignmentRepository;
import finki.db.tasty_tabs.repository.EmployeeRepository;
import finki.db.tasty_tabs.repository.ManagerRepository;
import finki.db.tasty_tabs.repository.ShiftRepository;
import finki.db.tasty_tabs.service.AssignmentService;
import finki.db.tasty_tabs.web.dto.CreateAssignmentDto;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final ManagerRepository managerRepository;
    private final EmployeeRepository employeeRepository;
    private final ShiftRepository shiftRepository;

    public AssignmentServiceImpl(AssignmentRepository assignmentRepository, ManagerRepository managerRepository, EmployeeRepository employeeRepository, ShiftRepository shiftRepository) {
        this.assignmentRepository = assignmentRepository;
        this.managerRepository = managerRepository;
        this.employeeRepository = employeeRepository;
        this.shiftRepository = shiftRepository;
    }

    private Manager getManagerByEmail(String email) {
        return managerRepository.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("Only managers can assign assignments."));
    }

    @Override
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    @Override
    public Assignment getAssignmentById(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new AssignmentNotFoundException(id));
    }

    @Override
    public Assignment createAssignment(CreateAssignmentDto dto, String managerEmail) {
        Manager manager = getManagerByEmail(managerEmail);
        Employee employee = employeeRepository.findById(dto.employeeId())
                .orElseThrow(() -> new EmployeeNotFoundException(dto.employeeId()));
        Shift shift = shiftRepository.findById(dto.shiftId())
                .orElseThrow(() -> new ShiftNotFoundException(dto.shiftId()));

        Assignment assignment = new Assignment(
                dto.clockInTime(),
                dto.clockOutTime(),
                manager,
                employee,
                shift
        );

        return assignmentRepository.save(assignment);
    }

    @Override
    public Assignment updateAssignment(Long id, CreateAssignmentDto dto, String managerEmail) {
        Manager manager = getManagerByEmail(managerEmail);
        Assignment assignment = getAssignmentById(id);

        Employee employee = employeeRepository.findById(dto.employeeId())
                .orElseThrow(() -> new EmployeeNotFoundException(dto.employeeId()));
        Shift shift = shiftRepository.findById(dto.shiftId())
                .orElseThrow(() -> new ShiftNotFoundException(dto.shiftId()));

        assignment.setClockInTime(dto.clockInTime());
        assignment.setClockOutTime(dto.clockOutTime());
        assignment.setManager(manager);
        assignment.setEmployee(employee);
        assignment.setShift(shift);

        return assignmentRepository.save(assignment);
    }

    @Override
    public void deleteAssignment(Long id, String managerEmail) {
        getManagerByEmail(managerEmail);
        if (!assignmentRepository.existsById(id)) {
            throw new AssignmentNotFoundException(id);
        }
        assignmentRepository.deleteById(id);
    }
    @Override
    public Assignment clockInShift(Long assignmentId, String employeeEmail, LocalDateTime clockInTime) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException( assignmentId));

        // Check if the logged-in employee is assigned to this assignment
        if (!assignment.getEmployee().getEmail().equals(employeeEmail)) {
            throw new AccessDeniedException("You can only clock in your own shift.");
        }

        assignment.setClockInTime(clockInTime);
        return assignmentRepository.save(assignment);
    }

    @Override
    public Assignment clockOutShift(Long assignmentId, String employeeEmail, LocalDateTime clockOutTime) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException(assignmentId));

        if (!assignment.getEmployee().getEmail().equals(employeeEmail)) {
            throw new AccessDeniedException("You can only clock out your own shift.");
        }

        if (assignment.getClockInTime() == null) {
            throw new IllegalStateException("Cannot clock out without clocking in first.");
        }

        assignment.setClockOutTime(clockOutTime);
        return assignmentRepository.save(assignment);
    }

}
