package finki.db.tasty_tabs.service;

import finki.db.tasty_tabs.entity.Employee;
import finki.db.tasty_tabs.web.dto.AssignmentDto;
import finki.db.tasty_tabs.web.dto.CreateEmployeeRequest;
import finki.db.tasty_tabs.web.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService { // New Service
    List<EmployeeDto> getAllEmployees();
    Employee createEmployee(CreateEmployeeRequest request);

    AssignmentDto getNextShiftForEmployee(Long employeeId);
}
