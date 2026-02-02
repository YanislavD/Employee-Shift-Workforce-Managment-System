package com.workforce.repository;

import com.workforce.domain.entity.Employee;
import com.workforce.domain.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    List<Employee> findByActiveTrue();

    List<Employee> findByRole(Role role);
}
