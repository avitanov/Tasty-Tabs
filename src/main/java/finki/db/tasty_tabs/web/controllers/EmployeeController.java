package finki.db.tasty_tabs.web.controllers;

import finki.db.tasty_tabs.entity.Employee;
import finki.db.tasty_tabs.service.EmployeeService;
import finki.db.tasty_tabs.web.dto.CreateEmployeeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees") // New Controller
class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // This endpoint would be secured in a real application to ensure only managers can access it.
    @PostMapping("/manager/create")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Employee> createEmployee(@RequestBody CreateEmployeeRequest request) {
        Employee newEmployee = employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEmployee);
    }
}