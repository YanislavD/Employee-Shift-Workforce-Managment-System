package com.workforce.controller;

import com.workforce.dto.AvailabilityRequest;
import com.workforce.dto.AvailabilityResponse;
import com.workforce.service.AvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availabilities")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @GetMapping
    public List<AvailabilityResponse> list(@RequestParam(required = false) Long employeeId) {
        if (employeeId != null) return availabilityService.findByEmployeeId(employeeId);
        return availabilityService.findAll();
    }

    @GetMapping("/{id}")
    public AvailabilityResponse get(@PathVariable Long id) {
        return availabilityService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvailabilityResponse create(@Valid @RequestBody AvailabilityRequest request) {
        return availabilityService.create(request);
    }

    @PutMapping("/{id}")
    public AvailabilityResponse update(@PathVariable Long id, @Valid @RequestBody AvailabilityRequest request) {
        return availabilityService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        availabilityService.delete(id);
    }
}
