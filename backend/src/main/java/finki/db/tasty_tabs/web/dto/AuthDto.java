package finki.db.tasty_tabs.web.dto;

public record AuthDto (
        String token,
        UserDto user
){
}
