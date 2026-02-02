package com.workforce.service;

import com.workforce.domain.entity.Availability;
import com.workforce.domain.entity.Employee;
import com.workforce.dto.AvailabilityRequest;
import com.workforce.dto.AvailabilityResponse;
import com.workforce.repository.AvailabilityRepository;
import com.workforce.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public List<AvailabilityResponse> findAll() {
        return availabilityRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AvailabilityResponse> findByEmployeeId(Long employeeId) {
        return availabilityRepository.findByEmployeeId(employeeId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AvailabilityResponse findById(Long id) {
        Availability a = availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability not found: " + id));
        return toResponse(a);
    }

    @Transactional
    public AvailabilityResponse create(AvailabilityRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found: " + request.getEmployeeId()));
        Availability a = Availability.builder()
                .employee(employee)
                .date(request.getDate())
                .available(request.getAvailable() != null ? request.getAvailable() : true)
                .build();
        a = availabilityRepository.save(a);
        return toResponse(a);
    }

    @Transactional
    public AvailabilityResponse update(Long id, AvailabilityRequest request) {
        Availability a = availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability not found: " + id));
        a.setDate(request.getDate());
        a.setAvailable(request.getAvailable() != null ? request.getAvailable() : true);
        a = availabilityRepository.save(a);
        return toResponse(a);
    }

    @Transactional
    public void delete(Long id) {
        if (!availabilityRepository.existsById(id)) throw new RuntimeException("Availability not found: " + id);
        availabilityRepository.deleteById(id);
    }

    private AvailabilityResponse toResponse(Availability a) {
        return AvailabilityResponse.builder()
                .id(a.getId())
                .employeeId(a.getEmployee().getId())
                .employeeName(a.getEmployee().getFullName())
                .date(a.getDate())
                .available(a.getAvailable())
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .build();
    }
}
