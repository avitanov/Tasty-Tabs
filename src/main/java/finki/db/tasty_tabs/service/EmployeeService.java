package finki.db.tasty_tabs.service;

import finki.db.tasty_tabs.entity.Employee;
import finki.db.tasty_tabs.web.dto.CreateEmployeeRequest;

public interface EmployeeService { // New Service
    Employee createEmployee(CreateEmployeeRequest request);
}
