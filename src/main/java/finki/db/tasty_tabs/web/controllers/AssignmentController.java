package finki.db.tasty_tabs.web.controllers;

import finki.db.tasty_tabs.entity.Assignment;
import finki.db.tasty_tabs.service.AssignmentService;
import finki.db.tasty_tabs.web.dto.AssignmentDto;
import finki.db.tasty_tabs.web.dto.CreateAssignmentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@Tag(name = "Assignment API", description = "Endpoints for managing shift assignments (Manager only)")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @Operation(summary = "Get all assignments")
    @GetMapping
    public List<Assignment> getAllAssignments() {
        return assignmentService.getAllAssignments();
    }

    @Operation(summary = "Get assignment by ID")
    @GetMapping("/{id}")
    public Assignment getAssignmentById(@PathVariable Long id) {
        return assignmentService.getAssignmentById(id);
    }

    @Operation(summary = "Create assignment (Manager only)")
    @PostMapping
    public Assignment createAssignment(@RequestBody CreateAssignmentDto dto, Authentication authentication) {
        String managerEmail = authentication.getName();
        return assignmentService.createAssignment(dto, managerEmail);
    }

    @Operation(summary = "Update assignment (Manager only)")
    @PutMapping("/{id}")
    public Assignment updateAssignment(@PathVariable Long id, @RequestBody CreateAssignmentDto dto, Authentication authentication) {
        String managerEmail = authentication.getName();
        return assignmentService.updateAssignment(id, dto, managerEmail);
    }

    @Operation(summary = "Delete assignment (Manager only)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id, Authentication authentication) {
        String managerEmail = authentication.getName();
        assignmentService.deleteAssignment(id, managerEmail);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Employee clocks in their shift")
    @PostMapping("/{id}/clockin")
    public Assignment clockInShift(
            @PathVariable Long id,
            @RequestParam(required = false) LocalDateTime clockInTime,
            Authentication authentication) {

        String employeeEmail = authentication.getName();
        LocalDateTime timeToSet = clockInTime != null ? clockInTime : LocalDateTime.now();
        return assignmentService.clockInShift(id, employeeEmail, timeToSet);
    }

    @Operation(summary = "Employee clocks out their shift")
    @PostMapping("/{id}/clockout")
    public Assignment clockOutShift(
            @PathVariable Long id,
            @RequestParam(required = false) LocalDateTime clockOutTime,
            Authentication authentication) {

        String employeeEmail = authentication.getName();
        LocalDateTime timeToSet = clockOutTime != null ? clockOutTime : LocalDateTime.now();
        return assignmentService.clockOutShift(id, employeeEmail, timeToSet);
    }

}

