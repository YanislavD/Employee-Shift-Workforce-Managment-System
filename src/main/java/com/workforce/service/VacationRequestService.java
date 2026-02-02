package com.workforce.service;

import com.workforce.domain.entity.Employee;
import com.workforce.domain.entity.VacationRequest;
import com.workforce.domain.enums.VacationRequestStatus;
import com.workforce.dto.VacationRequestDto;
import com.workforce.repository.EmployeeRepository;
import com.workforce.repository.VacationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VacationRequestService {

    private final VacationRequestRepository vacationRequestRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public List<VacationRequestDto> findAll() {
        return vacationRequestRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VacationRequestDto> findByEmployeeId(Long employeeId) {
        return vacationRequestRepository.findByEmployeeId(employeeId).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VacationRequestDto findById(Long id) {
        VacationRequest v = vacationRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacation request not found: " + id));
        return toDto(v);
    }

    @Transactional
    public VacationRequestDto create(VacationRequestDto dto) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found: " + dto.getEmployeeId()));
        VacationRequest v = VacationRequest.builder()
                .employee(employee)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(dto.getStatus() != null ? dto.getStatus() : VacationRequestStatus.PENDING)
                .reason(dto.getReason())
                .build();
        v = vacationRequestRepository.save(v);
        return toDto(v);
    }

    @Transactional
    public VacationRequestDto updateStatus(Long id, VacationRequestStatus status, Long approvedById, String rejectionReason) {
        VacationRequest v = vacationRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacation request not found: " + id));
        v.setStatus(status);
        if (approvedById != null) {
            Employee approver = employeeRepository.findById(approvedById)
                    .orElseThrow(() -> new RuntimeException("Approver not found: " + approvedById));
            v.setApprovedBy(approver);
        }
        if (rejectionReason != null) v.setRejectionReason(rejectionReason);
        v = vacationRequestRepository.save(v);
        return toDto(v);
    }

    @Transactional
    public void delete(Long id) {
        if (!vacationRequestRepository.existsById(id)) throw new RuntimeException("Vacation request not found: " + id);
        vacationRequestRepository.deleteById(id);
    }

    private VacationRequestDto toDto(VacationRequest v) {
        return VacationRequestDto.builder()
                .id(v.getId())
                .employeeId(v.getEmployee().getId())
                .employeeName(v.getEmployee().getFullName())
                .startDate(v.getStartDate())
                .endDate(v.getEndDate())
                .status(v.getStatus())
                .approvedById(v.getApprovedBy() != null ? v.getApprovedBy().getId() : null)
                .reason(v.getReason())
                .rejectionReason(v.getRejectionReason())
                .build();
    }
}
