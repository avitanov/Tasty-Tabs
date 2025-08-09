package finki.db.tasty_tabs.service;

import finki.db.tasty_tabs.entity.Shift;
import finki.db.tasty_tabs.web.dto.CreateShiftDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ShiftService {
    List<Shift> getAllShifts();
    Shift getShiftById(Long id);
    Shift createShift(CreateShiftDto dto, String username);
    Shift updateShift(Long id, CreateShiftDto dto, String username);
    void deleteShift(Long id, String username);
}