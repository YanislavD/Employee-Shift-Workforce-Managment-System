package com.workforce.dto;

import com.workforce.domain.enums.Role;
import com.workforce.domain.enums.ShiftStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftRequest {

    @NotNull(message = "Shift date is required")
    private LocalDate shiftDate;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @Builder.Default
    private ShiftStatus status = ShiftStatus.PLANNED;

    @NotNull(message = "Required role is required")
    private Role requiredRole;

    private Long employeeId;
}
