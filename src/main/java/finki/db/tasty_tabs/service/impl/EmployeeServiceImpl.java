package finki.db.tasty_tabs.service.impl;

import finki.db.tasty_tabs.entity.*;
import finki.db.tasty_tabs.repository.*;
import finki.db.tasty_tabs.service.EmployeeService;
import finki.db.tasty_tabs.web.dto.AssignmentDto;
import finki.db.tasty_tabs.web.dto.CreateEmployeeRequest;
import finki.db.tasty_tabs.web.dto.EmployeeDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
class EmployeeServiceImpl implements EmployeeService { // New Service Implementation

    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;
    private final FrontStaffRepository frontStaffRepository;
    private final BackStaffRepository backStaffRepository;
    private final StaffRoleRepository staffRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final AssignmentRepository assignmentRepository;

    EmployeeServiceImpl(UserRepository userRepository, ManagerRepository managerRepository, FrontStaffRepository frontStaffRepository, BackStaffRepository backStaffRepository, StaffRoleRepository staffRoleRepository, PasswordEncoder passwordEncoder, EmployeeRepository employeeRepository, AssignmentRepository assignmentRepository) {
        this.userRepository = userRepository;
        this.managerRepository = managerRepository;
        this.frontStaffRepository = frontStaffRepository;
        this.backStaffRepository = backStaffRepository;
        this.staffRoleRepository = staffRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream()
                .map(EmployeeDto::from)
                .toList();
    }

    @Override
    public AssignmentDto getNextShiftForEmployee(Long employeeId) {
        Assignment assignment = assignmentRepository.findFirstByEmployee_IdOrderByShiftStartAsc(employeeId).orElse(null);

        if (assignment == null) {
            throw new EntityNotFoundException("No upcoming shifts found for employee with id: " + employeeId);
        }

        return AssignmentDto.fromAssignment(assignment);
    }

    @Override
    @Transactional
    public Employee createEmployee(CreateEmployeeRequest request) {
        // In a real app, you'd check if email is unique and hash the password
        if (userRepository.existsByEmail(request.email())) {
             throw new IllegalArgumentException("Employee with email " + request.email() + " already exists.");
        }

        Employee employee;

        switch (request.employeeType()) {
            case MANAGER:
                Manager manager = new Manager();
                setCommonEmployeeFields(manager, request);

                employee = manager;
                break;
            case FRONT_STAFF:
                FrontStaff frontStaff = new FrontStaff();
                setCommonEmployeeFields(frontStaff, request);
                StaffRole frontStaffRole = staffRoleRepository.findById(request.staffRoleId())
                        .orElseThrow(() -> new EntityNotFoundException("StaffRole not found with id: " + request.staffRoleId()));
                frontStaff.setStaffRole(frontStaffRole);
                frontStaff.setTipPercent(request.tipPercent());

                employee = frontStaff;
                break;
            case BACK_STAFF:
                BackStaff backStaff = new BackStaff();
                setCommonEmployeeFields(backStaff, request);
                StaffRole backStaffRole = staffRoleRepository.findById(request.staffRoleId())
                        .orElseThrow(() -> new EntityNotFoundException("StaffRole not found with id: " + request.staffRoleId()));
                backStaff.setStaffRole(backStaffRole);

                employee = backStaff;
                break;
            default:
                throw new IllegalArgumentException("Invalid employee type: " + request.email());
        }

        return userRepository.save(employee);
    }

    private void setCommonEmployeeFields(Employee employee, CreateEmployeeRequest request) {
        employee.setEmail(request.email());
        employee.setPassword(passwordEncoder.encode(request.password()));
        employee.setStreet(request.street());
        employee.setCity(request.city());
        employee.setPhoneNumber(request.phoneNumber());
        employee.setNetSalary(request.netSalary());
        employee.setGrossSalary(request.grossSalary());
    }
}