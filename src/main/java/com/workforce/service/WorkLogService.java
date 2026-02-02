package com.workforce.service;

import com.workforce.domain.entity.Employee;
import com.workforce.domain.entity.Shift;
import com.workforce.domain.entity.WorkLog;
import com.workforce.dto.WorkLogRequest;
import com.workforce.dto.WorkLogResponse;
import com.workforce.repository.EmployeeRepository;
import com.workforce.repository.ShiftRepository;
import com.workforce.repository.WorkLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkLogService {

    private final WorkLogRepository workLogRepository;
    private final EmployeeRepository employeeRepository;
    private final ShiftRepository shiftRepository;

    @Transactional(readOnly = true)
    public List<WorkLogResponse> findAll() {
        return workLogRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<WorkLogResponse> findByEmployeeId(Long employeeId) {
        return workLogRepository.findByEmployeeId(employeeId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WorkLogResponse findById(Long id) {
        WorkLog w = workLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Work log not found: " + id));
        return toResponse(w);
    }

    @Transactional
    public WorkLogResponse create(WorkLogRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found: " + request.getEmployeeId()));
        Shift shift = shiftRepository.findById(request.getShiftId())
                .orElseThrow(() -> new RuntimeException("Shift not found: " + request.getShiftId()));
        if (workLogRepository.findByShiftId(shift.getId()).isPresent()) {
            throw new RuntimeException("Work log already exists for shift: " + shift.getId());
        }
        WorkLog w = WorkLog.builder()
                .employee(employee)
                .shift(shift)
                .actualStartTime(request.getActualStartTime())
                .actualEndTime(request.getActualEndTime())
                .notes(request.getNotes())
                .build();
        w.calculateWorkedMinutes();
        w = workLogRepository.save(w);
        return toResponse(w);
    }

    @Transactional
    public WorkLogResponse update(Long id, WorkLogRequest request) {
        WorkLog w = workLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Work log not found: " + id));
        w.setActualStartTime(request.getActualStartTime());
        w.setActualEndTime(request.getActualEndTime());
        w.setNotes(request.getNotes());
        w.calculateWorkedMinutes();
        w = workLogRepository.save(w);
        return toResponse(w);
    }

    @Transactional
    public void delete(Long id) {
        if (!workLogRepository.existsById(id)) throw new RuntimeException("Work log not found: " + id);
        workLogRepository.deleteById(id);
    }

    private WorkLogResponse toResponse(WorkLog w) {
        return WorkLogResponse.builder()
                .id(w.getId())
                .employeeId(w.getEmployee().getId())
                .employeeName(w.getEmployee().getFullName())
                .shiftId(w.getShift().getId())
                .actualStartTime(w.getActualStartTime())
                .actualEndTime(w.getActualEndTime())
                .workedMinutes(w.getWorkedMinutes())
                .notes(w.getNotes())
                .createdAt(w.getCreatedAt())
                .updatedAt(w.getUpdatedAt())
                .build();
    }
}
