package finki.db.tasty_tabs.web.controllers;

import finki.db.tasty_tabs.entity.Assignment;
import finki.db.tasty_tabs.service.AssignmentService;
import finki.db.tasty_tabs.web.dto.AssignmentDto;
import finki.db.tasty_tabs.web.dto.CreateAssignmentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<AssignmentDto> getAllAssignments() {
        return assignmentService.getAllAssignments()
                .stream()
                .map(AssignmentDto::fromAssignment)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get assignment by ID")
    @GetMapping("/{id}")
    public ResponseEntity<AssignmentDto> getAssignmentById(@PathVariable Long id) {
        return ResponseEntity.ok(AssignmentDto.fromAssignment(assignmentService.getAssignmentById(id)));
    }

    @Operation(summary = "Create assignment (Manager only)")
    @PostMapping
    public ResponseEntity<AssignmentDto> createAssignment(@RequestBody CreateAssignmentDto dto, Authentication authentication) {
        try {
            String managerEmail = authentication.getName();
            Assignment assignment = assignmentService.createAssignment(dto, managerEmail);
            return ResponseEntity.status(HttpStatus.CREATED).body(AssignmentDto.fromAssignment(assignment));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Update assignment (Manager only)")
    @PutMapping("/{id}")
    public ResponseEntity<AssignmentDto> updateAssignment(@PathVariable Long id, @RequestBody CreateAssignmentDto dto, Authentication authentication) {
        try {
            String managerEmail = authentication.getName();
            Assignment updated = assignmentService.updateAssignment(id, dto, managerEmail);
            return ResponseEntity.ok(AssignmentDto.fromAssignment(updated));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Delete assignment (Manager only)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id, Authentication authentication) {
        try {
            String managerEmail = authentication.getName();
            assignmentService.deleteAssignment(id, managerEmail);
            return ResponseEntity.noContent().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Employee clocks in their shift")
    @PostMapping("/{id}/clockin")
    public ResponseEntity<AssignmentDto> clockInShift(
            @PathVariable Long id,
            @RequestParam(required = false) LocalDateTime clockInTime,
            Authentication authentication) {
        try {
            String employeeEmail = authentication.getName();
            LocalDateTime timeToSet = clockInTime != null ? clockInTime : LocalDateTime.now();
            Assignment assignment = assignmentService.clockInShift(id, employeeEmail, timeToSet);
            return ResponseEntity.ok(AssignmentDto.fromAssignment(assignment));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Employee clocks out their shift")
    @PostMapping("/{id}/clockout")
    public ResponseEntity<AssignmentDto> clockOutShift(
            @PathVariable Long id,
            @RequestParam(required = false) LocalDateTime clockOutTime,
            Authentication authentication) {
        try {
            String employeeEmail = authentication.getName();
            LocalDateTime timeToSet = clockOutTime != null ? clockOutTime : LocalDateTime.now();
            Assignment assignment = assignmentService.clockOutShift(id, employeeEmail, timeToSet);
            return ResponseEntity.ok(AssignmentDto.fromAssignment(assignment));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}