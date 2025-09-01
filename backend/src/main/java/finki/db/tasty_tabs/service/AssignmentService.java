package finki.db.tasty_tabs.service;

import finki.db.tasty_tabs.entity.Assignment;
import finki.db.tasty_tabs.web.dto.CreateAssignmentDto;

import java.time.LocalDateTime;
import java.util.List;

public interface AssignmentService {
    List<Assignment> getAllAssignments();
    Assignment getAssignmentById(Long id);
    Assignment createAssignment(CreateAssignmentDto dto, String managerEmail);
    Assignment updateAssignment(Long id, CreateAssignmentDto dto, String managerEmail);
    void deleteAssignment(Long id, String managerEmail);
    // New method for employee to clock in/out
    Assignment clockInShift(Long assignmentId, String employeeEmail, LocalDateTime clockInTime);
    Assignment clockOutShift(Long assignmentId, String employeeEmail, LocalDateTime clockOutTime);
}
