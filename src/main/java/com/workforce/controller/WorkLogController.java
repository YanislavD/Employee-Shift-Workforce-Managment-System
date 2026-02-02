package com.workforce.controller;

import com.workforce.dto.WorkLogRequest;
import com.workforce.dto.WorkLogResponse;
import com.workforce.service.WorkLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/work-logs")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class WorkLogController {

    private final WorkLogService workLogService;

    @GetMapping
    public List<WorkLogResponse> list(@RequestParam(required = false) Long employeeId) {
        if (employeeId != null) return workLogService.findByEmployeeId(employeeId);
        return workLogService.findAll();
    }

    @GetMapping("/{id}")
    public WorkLogResponse get(@PathVariable Long id) {
        return workLogService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkLogResponse create(@Valid @RequestBody WorkLogRequest request) {
        return workLogService.create(request);
    }

    @PutMapping("/{id}")
    public WorkLogResponse update(@PathVariable Long id, @Valid @RequestBody WorkLogRequest request) {
        return workLogService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        workLogService.delete(id);
    }
}
