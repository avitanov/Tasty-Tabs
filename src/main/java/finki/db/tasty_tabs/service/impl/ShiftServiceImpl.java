package finki.db.tasty_tabs.service.impl;

import finki.db.tasty_tabs.entity.Assignment;
import finki.db.tasty_tabs.entity.Employee;
import finki.db.tasty_tabs.entity.Manager;
import finki.db.tasty_tabs.entity.Shift;
import finki.db.tasty_tabs.entity.exceptions.ShiftNotFoundException;
import finki.db.tasty_tabs.repository.AssignmentRepository;
import finki.db.tasty_tabs.repository.EmployeeRepository;
import finki.db.tasty_tabs.repository.ManagerRepository;
import finki.db.tasty_tabs.repository.ShiftRepository;
import finki.db.tasty_tabs.service.ShiftService;
import finki.db.tasty_tabs.web.dto.AssignmentDto;
import finki.db.tasty_tabs.web.dto.ClockInRequest;
import finki.db.tasty_tabs.web.dto.CreateShiftDto;
import finki.db.tasty_tabs.web.dto.ShiftDto;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShiftServiceImpl implements ShiftService{
    private final ShiftRepository shiftRepository;
    private final ManagerRepository managerRepository;

    public ShiftServiceImpl(ShiftRepository shiftRepository, ManagerRepository managerRepository) {
        this.shiftRepository = shiftRepository;
        this.managerRepository = managerRepository;
    }

    private Manager getManagerByUsername(String username) {
        return managerRepository.findByEmail(username)
                .orElseThrow(() -> new AccessDeniedException("Only managers can assign shifts."));
    }

    @Override
    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    @Override
    public Shift getShiftById(Long id) {
        return shiftRepository.findById(id)
                .orElseThrow(() -> new ShiftNotFoundException(id));
    }

    @Override
    public Shift createShift(CreateShiftDto dto, String username) {
        Manager manager = getManagerByUsername(username);
        Shift shift = new Shift(dto.date(), dto.start(), dto.end(), manager);
        return shiftRepository.save(shift);
    }

    @Override
    public Shift updateShift(Long id, CreateShiftDto dto, String username) {
        Manager manager = getManagerByUsername(username);
        Shift shift = getShiftById(id);

        shift.setDate(dto.date());
        shift.setStart(dto.start());
        shift.setEnd(dto.end());
        shift.setManager(manager);

        return shiftRepository.save(shift);
    }

    @Override
    public void deleteShift(Long id, String username) {
        getManagerByUsername(username);
        if (!shiftRepository.existsById(id)) {
            throw new ShiftNotFoundException(id);
        }
        shiftRepository.deleteById(id);
    }
}