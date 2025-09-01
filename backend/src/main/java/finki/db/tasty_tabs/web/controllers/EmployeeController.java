package finki.db.tasty_tabs.web.controllers;

import finki.db.tasty_tabs.entity.Employee;
import finki.db.tasty_tabs.service.EmployeeService;
import finki.db.tasty_tabs.web.dto.AssignmentDto;
import finki.db.tasty_tabs.web.dto.CreateEmployeeRequest;
import finki.db.tasty_tabs.web.dto.EmployeeDto;
import finki.db.tasty_tabs.web.dto.ShiftDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping()
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{employeeId}/shifts/next")
    public ResponseEntity<AssignmentDto> getNextShift(@PathVariable Long employeeId) {
        AssignmentDto nextShift = employeeService.getNextShiftForEmployee(employeeId);
        return ResponseEntity.ok(nextShift);
    }
}