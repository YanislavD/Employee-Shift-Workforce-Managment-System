package com.workforce.dto;

import com.workforce.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private Integer contractHoursPerWeek;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
