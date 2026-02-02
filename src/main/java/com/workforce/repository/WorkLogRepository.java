package com.workforce.repository;

import com.workforce.domain.entity.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {

    List<WorkLog> findByEmployeeId(Long employeeId);

    Optional<WorkLog> findByShiftId(Long shiftId);
}
