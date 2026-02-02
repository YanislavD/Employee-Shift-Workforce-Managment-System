package com.workforce.service;

import com.workforce.domain.entity.Employee;
import com.workforce.domain.entity.Shift;
import com.workforce.dto.ShiftRequest;
import com.workforce.dto.ShiftResponse;
import com.workforce.repository.EmployeeRepository;
import com.workforce.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public List<ShiftResponse> findAll() {
        return shiftRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ShiftResponse> findByDate(LocalDate date) {
        return shiftRepository.findByShiftDate(date).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ShiftResponse> findByEmployeeId(Long employeeId) {
        return shiftRepository.findByEmployeeId(employeeId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ShiftResponse findById(Long id) {
        Shift s = shiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift not found: " + id));
        return toResponse(s);
    }

    @Transactional
    public ShiftResponse create(ShiftRequest request) {
        Employee employee = null;
        if (request.getEmployeeId() != null) {
            employee = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found: " + request.getEmployeeId()));
        }
        Shift s = Shift.builder()
                .shiftDate(request.getShiftDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(request.getStatus() != null ? request.getStatus() : com.workforce.domain.enums.ShiftStatus.PLANNED)
                .requiredRole(request.getRequiredRole())
                .employee(employee)
                .build();
        s = shiftRepository.save(s);
        return toResponse(s);
    }

    @Transactional
    public ShiftResponse update(Long id, ShiftRequest request) {
        Shift s = shiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift not found: " + id));
        Employee employee = null;
        if (request.getEmployeeId() != null) {
            employee = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found: " + request.getEmployeeId()));
        }
        s.setShiftDate(request.getShiftDate());
        s.setStartTime(request.getStartTime());
        s.setEndTime(request.getEndTime());
        s.setStatus(request.getStatus() != null ? request.getStatus() : s.getStatus());
        s.setRequiredRole(request.getRequiredRole());
        s.setEmployee(employee);
        s = shiftRepository.save(s);
        return toResponse(s);
    }

    @Transactional
    public void delete(Long id) {
        if (!shiftRepository.existsById(id)) throw new RuntimeException("Shift not found: " + id);
        shiftRepository.deleteById(id);
    }

    private ShiftResponse toResponse(Shift s) {
        return ShiftResponse.builder()
                .id(s.getId())
                .shiftDate(s.getShiftDate())
                .startTime(s.getStartTime())
                .endTime(s.getEndTime())
                .status(s.getStatus())
                .requiredRole(s.getRequiredRole())
                .employeeId(s.getEmployee() != null ? s.getEmployee().getId() : null)
                .employeeName(s.getEmployee() != null ? s.getEmployee().getFullName() : null)
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
}
