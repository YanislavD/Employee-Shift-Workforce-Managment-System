package com.workforce.controller;

import com.workforce.domain.enums.VacationRequestStatus;
import com.workforce.dto.VacationRequestDto;
import com.workforce.service.VacationRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vacation-requests")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class VacationRequestController {

    private final VacationRequestService vacationRequestService;

    @GetMapping
    public List<VacationRequestDto> list(@RequestParam(required = false) Long employeeId) {
        if (employeeId != null) return vacationRequestService.findByEmployeeId(employeeId);
        return vacationRequestService.findAll();
    }

    @GetMapping("/{id}")
    public VacationRequestDto get(@PathVariable Long id) {
        return vacationRequestService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VacationRequestDto create(@Valid @RequestBody VacationRequestDto dto) {
        return vacationRequestService.create(dto);
    }

    @PatchMapping("/{id}/status")
    public VacationRequestDto updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        String statusStr = (String) body.get("status");
        VacationRequestStatus status = VacationRequestStatus.valueOf(statusStr);
        Long approvedById = body.get("approvedById") != null ? Long.valueOf(body.get("approvedById").toString()) : null;
        String rejectionReason = (String) body.get("rejectionReason");
        return vacationRequestService.updateStatus(id, status, approvedById, rejectionReason);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        vacationRequestService.delete(id);
    }
}
