package finki.db.tasty_tabs.web.dto;
import finki.db.tasty_tabs.entity.Manager;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
public record ManagerDto(
        Long id,
        String email
        ){
    public static ManagerDto from(Manager manager){
        return new ManagerDto(manager.getId(), manager.getEmail());
    }
}