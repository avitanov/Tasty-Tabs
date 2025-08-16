package finki.db.tasty_tabs.web.dto;

import finki.db.tasty_tabs.entity.User;

public record UserDto(
        Long id,
        String email,
        String street,
        String city,
        String phoneNumber
) {
    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getStreet(),
                user.getCity(),
                user.getPhoneNumber()
        );
    }
}
