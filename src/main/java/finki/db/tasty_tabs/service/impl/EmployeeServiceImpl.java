package finki.db.tasty_tabs.service.impl;

import finki.db.tasty_tabs.entity.*;
import finki.db.tasty_tabs.repository.*;
import finki.db.tasty_tabs.service.EmployeeService;
import finki.db.tasty_tabs.web.dto.CreateEmployeeRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static finki.db.tasty_tabs.entity.EmployeeType.*;

@Service
@Transactional
class EmployeeServiceImpl implements EmployeeService { // New Service Implementation

    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;
    private final FrontStaffRepository frontStaffRepository;
    private final BackStaffRepository backStaffRepository;
    private final StaffRoleRepository staffRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeServiceImpl(UserRepository userRepository, ManagerRepository managerRepository, FrontStaffRepository frontStaffRepository, BackStaffRepository backStaffRepository, StaffRoleRepository staffRoleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.managerRepository = managerRepository;
        this.frontStaffRepository = frontStaffRepository;
        this.backStaffRepository = backStaffRepository;
        this.staffRoleRepository = staffRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
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
                employee = managerRepository.save(manager);
                break;
            case FRONT_STAFF:
                FrontStaff frontStaff = new FrontStaff();
                setCommonEmployeeFields(frontStaff, request);
                StaffRole frontStaffRole = staffRoleRepository.findById(request.staffRoleId())
                        .orElseThrow(() -> new EntityNotFoundException("StaffRole not found with id: " + request.staffRoleId()));
                frontStaff.setStaffRole(frontStaffRole);
                frontStaff.setTipPercent(request.tipPercent());
                employee = frontStaffRepository.save(frontStaff);
                break;
            case BACK_STAFF:
                BackStaff backStaff = new BackStaff();
                setCommonEmployeeFields(backStaff, request);
                StaffRole backStaffRole = staffRoleRepository.findById(request.staffRoleId())
                        .orElseThrow(() -> new EntityNotFoundException("StaffRole not found with id: " + request.staffRoleId()));
                backStaff.setStaffRole(backStaffRole);
                employee = backStaffRepository.save(backStaff);
                break;
            default:
                throw new IllegalArgumentException("Invalid employee type: " + request.email());
        }

        return employee;
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