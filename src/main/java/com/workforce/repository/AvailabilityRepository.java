package com.workforce.repository;

import com.workforce.domain.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    List<Availability> findByEmployeeId(Long employeeId);

    List<Availability> findByEmployeeIdAndDateBetween(Long employeeId, LocalDate start, LocalDate end);

    Optional<Availability> findByEmployeeIdAndDate(Long employeeId, LocalDate date);
}
