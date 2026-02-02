package com.workforce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkLogResponse {

    private Long id;
    private Long employeeId;
    private String employeeName;
    private Long shiftId;
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;
    private Long workedMinutes;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
