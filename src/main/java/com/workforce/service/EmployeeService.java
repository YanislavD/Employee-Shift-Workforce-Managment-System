package com.workforce.service;

import com.workforce.domain.entity.Employee;
import com.workforce.domain.enums.Role;
import com.workforce.dto.EmployeeRequest;
import com.workforce.dto.EmployeeResponse;
import com.workforce.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public List<EmployeeResponse> findAll(boolean activeOnly) {
        List<Employee> list = activeOnly ? employeeRepository.findByActiveTrue() : employeeRepository.findAll();
        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeResponse findById(Long id) {
        Employee e = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + id));
        return toResponse(e);
    }

    @Transactional
    public EmployeeResponse create(EmployeeRequest request) {
        if (employeeRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Employee with email already exists: " + request.getEmail());
        }
        Employee e = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .role(request.getRole())
                .contractHoursPerWeek(request.getContractHoursPerWeek())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();
        e = employeeRepository.save(e);
        return toResponse(e);
    }

    @Transactional
    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee e = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + id));
        employeeRepository.findByEmail(request.getEmail()).ifPresent(other -> {
            if (!other.getId().equals(id)) throw new RuntimeException("Email already in use: " + request.getEmail());
        });
        e.setFirstName(request.getFirstName());
        e.setLastName(request.getLastName());
        e.setEmail(request.getEmail());
        e.setRole(request.getRole());
        e.setContractHoursPerWeek(request.getContractHoursPerWeek());
        e.setActive(request.getActive() != null ? request.getActive() : true);
        e = employeeRepository.save(e);
        return toResponse(e);
    }

    @Transactional
    public void delete(Long id) {
        if (!employeeRepository.existsById(id)) throw new RuntimeException("Employee not found: " + id);
        employeeRepository.deleteById(id);
    }

    private EmployeeResponse toResponse(Employee e) {
        return EmployeeResponse.builder()
                .id(e.getId())
                .firstName(e.getFirstName())
                .lastName(e.getLastName())
                .email(e.getEmail())
                .role(e.getRole())
                .contractHoursPerWeek(e.getContractHoursPerWeek())
                .active(e.getActive())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
