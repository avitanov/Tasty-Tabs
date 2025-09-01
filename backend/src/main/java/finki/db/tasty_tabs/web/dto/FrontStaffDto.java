package finki.db.tasty_tabs.web.dto;

import finki.db.tasty_tabs.entity.FrontStaff;

public record FrontStaffDto(
        Long id,
        String email,
        String street,
        String city,
        String phoneNumber,
        Double netSalary,
        Double grossSalary,
        Double tipPercent,
        String staffRoleName
) {
    public static FrontStaffDto from(FrontStaff user) {
        return new FrontStaffDto(
                user.getId(),
                user.getEmail(),
                user.getStreet(),
                user.getCity(),
                user.getPhoneNumber(),
                user.getNetSalary(),
                user.getGrossSalary(),
                user.getTipPercent(),     // Предпоставуваме дека User има tipPercent
                user.getStaffRole().getName()   // Предпоставуваме дека User има staffRoleName
        );
    }
}
