package finki.db.tasty_tabs.web.dto;

import finki.db.tasty_tabs.entity.EmployeeType;

public record CreateEmployeeRequest(
        String email,
        String password,
        String street,
        String city,
        String phoneNumber,
        Double netSalary,
        Double grossSalary,
        EmployeeType employeeType,
        Long staffRoleId, // Only for FrontStaff/BackStaff
        Double tipPercent // Only for FrontStaff
) {
}
