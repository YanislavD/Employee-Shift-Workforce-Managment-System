package com.workforce.controller;

import com.workforce.dto.ShiftRequest;
import com.workforce.dto.ShiftResponse;
import com.workforce.service.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class ShiftController {

    private final ShiftService shiftService;

    @GetMapping
    public List<ShiftResponse> list(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Long employeeId) {
        if (date != null) return shiftService.findByDate(date);
        if (employeeId != null) return shiftService.findByEmployeeId(employeeId);
        return shiftService.findAll();
    }

    @GetMapping("/{id}")
    public ShiftResponse get(@PathVariable Long id) {
        return shiftService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShiftResponse create(@Valid @RequestBody ShiftRequest request) {
        return shiftService.create(request);
    }

    @PutMapping("/{id}")
    public ShiftResponse update(@PathVariable Long id, @Valid @RequestBody ShiftRequest request) {
        return shiftService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        shiftService.delete(id);
    }
}
