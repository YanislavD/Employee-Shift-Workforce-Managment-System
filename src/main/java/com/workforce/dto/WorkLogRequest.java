package com.workforce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkLogRequest {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Shift ID is required")
    private Long shiftId;

    @NotNull(message = "Actual start time is required")
    private LocalDateTime actualStartTime;

    @NotNull(message = "Actual end time is required")
    private LocalDateTime actualEndTime;

    private String notes;
}
