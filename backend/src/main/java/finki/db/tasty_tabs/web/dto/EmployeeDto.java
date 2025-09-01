package finki.db.tasty_tabs.web.dto;

import finki.db.tasty_tabs.entity.Employee;
import finki.db.tasty_tabs.entity.UserType;

import java.time.LocalDateTime;

public record EmployeeDto(
        Long id,
        String email,
        String street,
        String city,
        String phoneNumber,
        Double netSalary,
        Double grossSalary,
        UserType userType
) {
    public static EmployeeDto from(Employee employee) {
        return new EmployeeDto(
                employee.getId(),
                employee.getEmail(),
                employee.getStreet(),
                employee.getCity(),
                employee.getPhoneNumber(),
                employee.getNetSalary(),
                employee.getGrossSalary(),
                employee.getUserType()
        );
    }
}
