package com.workforce.repository;

import com.workforce.domain.entity.Shift;
import com.workforce.domain.enums.ShiftStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

    List<Shift> findByShiftDate(LocalDate shiftDate);

    List<Shift> findByEmployeeId(Long employeeId);

    List<Shift> findByEmployeeIdAndShiftDateBetween(Long employeeId, LocalDate start, LocalDate end);

    List<Shift> findByStatus(ShiftStatus status);
}
