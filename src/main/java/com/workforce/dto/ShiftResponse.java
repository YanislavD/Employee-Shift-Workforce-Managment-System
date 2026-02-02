package com.workforce.dto;

import com.workforce.domain.enums.Role;
import com.workforce.domain.enums.ShiftStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftResponse {

    private Long id;
    private LocalDate shiftDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private ShiftStatus status;
    private Role requiredRole;
    private Long employeeId;
    private String employeeName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
