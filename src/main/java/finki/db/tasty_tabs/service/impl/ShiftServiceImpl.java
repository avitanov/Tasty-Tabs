package finki.db.tasty_tabs.service.impl;

import finki.db.tasty_tabs.entity.Assignment;
import finki.db.tasty_tabs.entity.Employee;
import finki.db.tasty_tabs.entity.Manager;
import finki.db.tasty_tabs.entity.Shift;
import finki.db.tasty_tabs.repository.AssignmentRepository;
import finki.db.tasty_tabs.repository.EmployeeRepository;
import finki.db.tasty_tabs.repository.ManagerRepository;
import finki.db.tasty_tabs.repository.ShiftRepository;
import finki.db.tasty_tabs.service.ShiftService;
import finki.db.tasty_tabs.web.dto.AssignmentDto;
import finki.db.tasty_tabs.web.dto.ClockInRequest;
import finki.db.tasty_tabs.web.dto.CreateShiftRequest;
import finki.db.tasty_tabs.web.dto.ShiftDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@Transactional
class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;
    private final AssignmentRepository assignmentRepository;
    private final ManagerRepository managerRepository;
    private final EmployeeRepository employeeRepository;

    public ShiftServiceImpl(ShiftRepository shiftRepository, AssignmentRepository assignmentRepository, ManagerRepository managerRepository, EmployeeRepository employeeRepository) {
        this.shiftRepository = shiftRepository;
        this.assignmentRepository = assignmentRepository;
        this.managerRepository = managerRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public AssignmentDto clockIn(ClockInRequest clockInRequest) {
        Assignment assignment = assignmentRepository.findByEmployeeIdAndShiftId(clockInRequest.getEmployeeId(), clockInRequest.getShiftId())
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found for employee " + clockInRequest.getEmployeeId() + " and shift " + clockInRequest.getShiftId()));

        if (assignment.getClockInTime() != null) {
            throw new IllegalStateException("Employee has already clocked in for this shift.");
        }

        assignment.setClockInTime(LocalDateTime.now());
        Assignment savedAssignment = assignmentRepository.save(assignment);
        
        // Manual mapping to DTO
        AssignmentDto dto = new AssignmentDto();
        dto.setId(savedAssignment.getId());
        dto.setClockInTime(savedAssignment.getClockInTime());
        dto.setClockOutTime(savedAssignment.getClockOutTime());
        dto.setManagerId(savedAssignment.getManager().getId());
        dto.setEmployeeId(savedAssignment.getEmployee().getId());
        dto.setShiftId(savedAssignment.getShift().getId());
        return dto;
    }

    @Override
    public ShiftDto createAndAssignShift(CreateShiftRequest request) {
        Manager manager = managerRepository.findById(request.getShift().getManagerId())
                .orElseThrow(() -> new EntityNotFoundException("Manager not found with id: " + request.getShift().getManagerId()));

        Shift shift = new Shift();
        shift.setManager(manager);
        shift.setDate(request.getShift().getDate());
        shift.setStart(request.getShift().getStart());
        shift.setEnd(request.getShift().getEnd());
        Shift savedShift = shiftRepository.save(shift);

        for (Long employeeId : request.getEmployeeIds()) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + employeeId));
            Assignment assignment = new Assignment();
            assignment.setShift(savedShift);
            assignment.setEmployee(employee);
            assignment.setManager(manager);
            assignmentRepository.save(assignment);
        }
        
        ShiftDto dto = new ShiftDto();
        dto.setId(savedShift.getId());
        dto.setDate(savedShift.getDate());
        dto.setStart(savedShift.getStart());
        dto.setEnd(savedShift.getEnd());
        dto.setManagerId(savedShift.getManager().getId());
        return dto;
    }
}