package finki.db.tasty_tabs.service;

import finki.db.tasty_tabs.web.dto.AssignmentDto;
import finki.db.tasty_tabs.web.dto.ClockInRequest;
import finki.db.tasty_tabs.web.dto.CreateShiftRequest;
import finki.db.tasty_tabs.web.dto.ShiftDto;

public interface ShiftService {
    AssignmentDto clockIn(ClockInRequest clockInRequest);
    ShiftDto createAndAssignShift(CreateShiftRequest request);
}