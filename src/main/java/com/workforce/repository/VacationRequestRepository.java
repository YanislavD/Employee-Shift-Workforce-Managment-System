package com.workforce.repository;

import com.workforce.domain.entity.VacationRequest;
import com.workforce.domain.enums.VacationRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {

    List<VacationRequest> findByEmployeeId(Long employeeId);

    List<VacationRequest> findByStatus(VacationRequestStatus status);
}
